package com.eripe14.combatlog;

import com.eripe14.combatlog.combatlog.CombatLogManager;
import com.eripe14.combatlog.commands.handler.InvalidUsage;
import com.eripe14.combatlog.commands.handler.PermissionMessage;
import com.eripe14.combatlog.commands.implementation.TagCommand;
import com.eripe14.combatlog.commands.implementation.UnTagCommand;
import com.eripe14.combatlog.config.ConfigLoader;
import com.eripe14.combatlog.config.MessageConfig;
import com.eripe14.combatlog.config.PluginConfig;
import com.eripe14.combatlog.listeners.entity.EntityDamageByEntityListener;
import com.eripe14.combatlog.listeners.entity.EntityDeathListener;
import com.eripe14.combatlog.listeners.player.PlayerCommandPreprocessListener;
import com.eripe14.combatlog.listeners.player.PlayerQuitListener;
import com.eripe14.combatlog.scheduler.CombatLogManageTask;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.tools.BukkitPlayerArgument;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.stream.Stream;

public class CombatLogPlugin extends JavaPlugin {

    private final File messagePath = new File(this.getDataFolder( ), "messages.yml");
    private final File configPath = new File(this.getDataFolder( ), "config.yml");
    private MessageConfig messageConfig;
    private PluginConfig pluginConfig;
    private CombatLogManager combatLogManager;
    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onLoad() {
        this.messageConfig = new ConfigLoader(messagePath).loadMessageConfig( );
        this.pluginConfig = new ConfigLoader(configPath).loadPluginConfig( );
    }

    @Override
    public void onEnable() {
        this.combatLogManager = new CombatLogManager( );

        this.liteCommands = LiteBukkitFactory.builder(this.getServer(), "eternal-combatlog")
                .argument(Player.class, new BukkitPlayerArgument<>(this.getServer( ), this.messageConfig.cantFindPlayer))

                .commandInstance(new TagCommand(this.combatLogManager, this.messageConfig, this.pluginConfig))
                .commandInstance(new UnTagCommand(this.combatLogManager, this.messageConfig))

                .invalidUsageHandler(new InvalidUsage( ))
                .permissionHandler(new PermissionMessage(this.messageConfig))

                .register( );

        Bukkit.getScheduler().runTaskTimer(this,
                new CombatLogManageTask(this.combatLogManager,
                this.messageConfig), 20L, 20L);


        Stream.of(
                new EntityDamageByEntityListener(this.combatLogManager, this.messageConfig, this.pluginConfig),
                new EntityDeathListener(this.combatLogManager, this.messageConfig),
                new PlayerCommandPreprocessListener(this.combatLogManager, this.pluginConfig, this.messageConfig),
                new PlayerQuitListener(this.combatLogManager, this.messageConfig)
        ).forEach(listener -> this.getServer( ).getPluginManager( ).registerEvents(listener, this));
    }

    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }

    public MessageConfig getMessageConfig() {
        return messageConfig;
    }

    public LiteCommands<CommandSender> getLiteCommands() {
        return liteCommands;
    }
}
