package com.eternalcode.combat;

import com.eternalcode.combat.bridge.BridgeService;
import com.eternalcode.combat.fight.drop.DropKeepInventoryService;
import com.eternalcode.combat.fight.FightManager;
import com.eternalcode.combat.fight.drop.DropService;
import com.eternalcode.combat.fight.effect.FightEffectService;
import com.eternalcode.combat.fight.tagout.FightTagOutService;
import com.eternalcode.combat.fight.pearl.FightPearlService;
import com.eternalcode.combat.handler.InvalidUsageHandlerImpl;
import com.eternalcode.combat.handler.MissingPermissionHandlerImpl;
import com.eternalcode.combat.config.ConfigService;
import com.eternalcode.combat.config.implementation.PluginConfig;
import com.eternalcode.combat.fight.drop.DropController;
import com.eternalcode.combat.fight.drop.DropKeepInventoryServiceImpl;
import com.eternalcode.combat.fight.drop.DropServiceImpl;
import com.eternalcode.combat.fight.drop.impl.PercentDropModifier;
import com.eternalcode.combat.fight.drop.impl.PlayersHealthDropModifier;
import com.eternalcode.combat.fight.FightTagCommand;
import com.eternalcode.combat.fight.controller.FightActionBlockerController;
import com.eternalcode.combat.fight.controller.FightMessageController;
import com.eternalcode.combat.fight.controller.FightTagController;
import com.eternalcode.combat.fight.controller.FightUnTagController;
import com.eternalcode.combat.fight.effect.FightEffectController;
import com.eternalcode.combat.event.EventCaller;
import com.eternalcode.combat.fight.FightManagerImpl;
import com.eternalcode.combat.fight.FightTask;
import com.eternalcode.combat.fight.bossbar.FightBossBarService;
import com.eternalcode.combat.fight.effect.FightEffectServiceImpl;
import com.eternalcode.combat.fight.logout.LogoutController;
import com.eternalcode.combat.fight.logout.LogoutService;
import com.eternalcode.combat.fight.pearl.FightPearlController;
import com.eternalcode.combat.fight.pearl.FightPearlServiceImpl;
import com.eternalcode.combat.fight.tagout.FightTagOutController;
import com.eternalcode.combat.fight.tagout.FightTagOutServiceImpl;
import com.eternalcode.combat.fight.tagout.FightTagOutCommand;
import com.eternalcode.combat.notification.NotificationAnnouncer;
import com.eternalcode.combat.region.RegionController;
import com.eternalcode.combat.region.RegionProvider;
import com.eternalcode.combat.updater.UpdaterNotificationController;
import com.eternalcode.combat.updater.UpdaterService;
import com.eternalcode.commons.adventure.AdventureLegacyColorPostProcessor;
import com.eternalcode.commons.adventure.AdventureLegacyColorPreProcessor;
import com.google.common.base.Stopwatch;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bstats.bukkit.Metrics;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public final class CombatPlugin extends JavaPlugin implements EternalCombatApi {

    private static final String FALLBACK_PREFIX = "eternalcombat";
    private static final int BSTATS_METRICS_ID = 17803;

    private FightManager fightManager;
    private FightPearlService fightPearlService;
    private FightTagOutService fightTagOutService;
    private FightEffectService fightEffectService;

    private LogoutService logoutService;

    private DropService dropService;
    private DropKeepInventoryService dropKeepInventoryService;

    private RegionProvider regionProvider;

    private PluginConfig pluginConfig;

    private AudienceProvider audienceProvider;
    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        Stopwatch started = Stopwatch.createStarted();
        Server server = this.getServer();

        File dataFolder = this.getDataFolder();

        ConfigService configService = new ConfigService();

        EventCaller eventCaller = new EventCaller(server);

        this.pluginConfig = configService.create(PluginConfig.class, new File(dataFolder, "config.yml"));

        this.fightManager = new FightManagerImpl(eventCaller);
        this.fightPearlService = new FightPearlServiceImpl(this.pluginConfig.pearl);
        this.fightTagOutService = new FightTagOutServiceImpl();
        this.fightEffectService = new FightEffectServiceImpl();
        this.logoutService = new LogoutService();
        this.dropService = new DropServiceImpl();
        this.dropKeepInventoryService = new DropKeepInventoryServiceImpl();

        UpdaterService updaterService = new UpdaterService(this.getDescription());

        this.audienceProvider = BukkitAudiences.create(this);
        MiniMessage miniMessage = MiniMessage.builder()
            .postProcessor(new AdventureLegacyColorPostProcessor())
            .preProcessor(new AdventureLegacyColorPreProcessor())
            .build();

        FightBossBarService fightBossBarService = new FightBossBarService(this.pluginConfig, this.audienceProvider, miniMessage);

        BridgeService bridgeService = new BridgeService(this.pluginConfig, server.getPluginManager(), this.getLogger(), this);
        bridgeService.init(this.fightManager, server);
        this.regionProvider = bridgeService.getRegionProvider();

        NotificationAnnouncer notificationAnnouncer = new NotificationAnnouncer(this.audienceProvider, miniMessage);

        this.liteCommands = LiteBukkitFactory.builder(FALLBACK_PREFIX, this, server)
            .message(LiteBukkitMessages.PLAYER_NOT_FOUND, this.pluginConfig.messages.playerNotFound)
            .message(LiteBukkitMessages.PLAYER_ONLY, this.pluginConfig.messages.admin.onlyForPlayers)

            .invalidUsage(new InvalidUsageHandlerImpl(this.pluginConfig, notificationAnnouncer))
            .missingPermission(new MissingPermissionHandlerImpl(this.pluginConfig, notificationAnnouncer))

            .commands(
                new FightTagCommand(this.fightManager, notificationAnnouncer, this.pluginConfig),
                new FightTagOutCommand(this.fightTagOutService, notificationAnnouncer, this.pluginConfig),
                new EternalCombatReloadCommand(configService, notificationAnnouncer)
            )

            .build();

        FightTask fightTask = new FightTask(server, this.pluginConfig, this.fightManager, fightBossBarService, notificationAnnouncer);
        this.getServer().getScheduler().runTaskTimer(this, fightTask, 20L, 20L);

        new Metrics(this, BSTATS_METRICS_ID);

        Stream.of(
            new PercentDropModifier(this.pluginConfig.dropSettings),
            new PlayersHealthDropModifier(this.pluginConfig.dropSettings, this.logoutService)
        ).forEach(this.dropService::registerModifier);


        Stream.of(
            new DropController(this.dropService, this.dropKeepInventoryService, this.pluginConfig.dropSettings, this.fightManager),
            new FightTagController(this.fightManager, this.pluginConfig),
            new LogoutController(this.fightManager, this.logoutService, notificationAnnouncer, this.pluginConfig),
            new FightUnTagController(this.fightManager, this.pluginConfig, this.logoutService),
            new FightActionBlockerController(this.fightManager, notificationAnnouncer, this.pluginConfig),
            new FightPearlController(this.pluginConfig.pearl, notificationAnnouncer, this.fightManager, this.fightPearlService),
            new UpdaterNotificationController(updaterService, this.pluginConfig, this.audienceProvider, miniMessage),
            new RegionController(notificationAnnouncer, this.regionProvider, this.fightManager, this.pluginConfig),
            new FightEffectController(this.pluginConfig.effect, this.fightEffectService, this.fightManager, this.getServer()),
            new FightTagOutController(this.fightTagOutService),
            new FightMessageController(this.fightManager, notificationAnnouncer, fightBossBarService, this.pluginConfig, this.getServer())
        ).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));

        EternalCombatProvider.initialize(this);

        long millis = started.elapsed(TimeUnit.MILLISECONDS);
        this.getLogger().info("Successfully loaded EternalCombat in " + millis + "ms");
    }

    @Override
    public void onDisable() {
        EternalCombatProvider.deinitialize();

        if (this.liteCommands != null) {
            this.liteCommands.unregister();
        }

        if (this.audienceProvider != null) {
            this.audienceProvider.close();
        }

        this.fightManager.untagAll();
    }

    @Override
    public FightManager getFightManager() {
        return this.fightManager;
    }

    @Override
    public RegionProvider getRegionProvider() {
        return this.regionProvider;
    }

    @Override
    public FightPearlService getFightPearlService() {
        return this.fightPearlService;
    }

    @Override
    public FightTagOutService getFightTagOutService() {
        return this.fightTagOutService;
    }

    @Override
    public FightEffectService getFightEffectService() {
        return this.fightEffectService;
    }

    @Override
    public DropService getDropService() {
        return this.dropService;
    }

    @Override
    public DropKeepInventoryService getDropKeepInventoryService() {
        return this.dropKeepInventoryService;
    }
}
