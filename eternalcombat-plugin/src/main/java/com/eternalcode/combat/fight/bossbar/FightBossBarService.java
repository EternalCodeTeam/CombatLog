package com.eternalcode.combat.fight.bossbar;

import com.eternalcode.combat.config.implementation.PluginConfig;
import com.eternalcode.combat.fight.FightTag;
import com.eternalcode.combat.notification.implementation.BossBarNotification;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import panda.utilities.text.Formatter;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

public class FightBossBarService {

    private final PluginConfig pluginConfig;
    private final FightBossBarRegistry bossBarRegistry = new FightBossBarRegistry();
    private final AudienceProvider audienceProvider;
    private final MiniMessage miniMessage;

    public FightBossBarService(PluginConfig pluginConfig, AudienceProvider audienceProvider, MiniMessage miniMessage) {
        this.pluginConfig = pluginConfig;
        this.audienceProvider = audienceProvider;
        this.miniMessage = miniMessage;
    }

    public void hide(FightTag fightTag, FightBossBar fightBossBar) {
        Audience audience = fightBossBar.audience();
        BossBar bossBar = fightBossBar.bossBar();
        UUID taggedPlayer = fightTag.getTaggedPlayer();

        audience.hideBossBar(bossBar);
        this.bossBarRegistry.remove(taggedPlayer);
    }

    public void hide(UUID playerUuid) {
        Optional<FightBossBar> bossBar = this.bossBarRegistry.getFightBossBar(playerUuid);

        if (bossBar.isPresent()) {
            FightBossBar fightBossBar = bossBar.get();
            Audience audience = fightBossBar.audience();
            BossBar bar = fightBossBar.bossBar();

            audience.hideBossBar(bar);
            this.bossBarRegistry.remove(playerUuid);
        }
    }

    public void send(Player player, FightTag fightTag, BossBarNotification bossBarNotification, Formatter formatter) {
        UUID playerUniqueId = player.getUniqueId();

        FightBossBar fightBossBar = this.bossBarRegistry.getFightBossBar(playerUniqueId)
            .orElseGet(() -> this.create(player, bossBarNotification, formatter));

        if (!this.bossBarRegistry.hasFightBossBar(playerUniqueId)) {
            this.show(player, fightBossBar);
            return;
        }

        String message = formatter.format(bossBarNotification.message());

        this.update(fightTag, fightBossBar, message);
    }

    private void update(FightTag fightTag, FightBossBar fightBossBar, String message) {
        if (fightTag.isExpired()) {
            this.hide(fightTag, fightBossBar);
            return;
        }

        BossBar bossBar = fightBossBar.bossBar();

        long combatDurationMillis = this.pluginConfig.settings.combatDuration.toMillis();
        long remainingDurationMillis = fightTag.getRemainingDuration().toMillis();

        float progress = (float) remainingDurationMillis / combatDurationMillis;

        if (progress > 1.0F) {
            progress = 1.0F;
        }

        Component name = this.miniMessage.deserialize(message);
        bossBar.name(name);
        bossBar.progress(progress);
    }

    private void show(Player player, FightBossBar fightBossBar) {
        UUID playerUniqueId = player.getUniqueId();

        Audience audience = this.audienceProvider.player(playerUniqueId);
        BossBar bossBar = fightBossBar.bossBar();

        audience.showBossBar(bossBar);
        this.bossBarRegistry.add(playerUniqueId, fightBossBar);
    }

    private FightBossBar create(Player player, BossBarNotification bossBarNotification, Formatter formatter) {
        Audience audience = this.audienceProvider.player(player.getUniqueId());

        Component name = this.miniMessage.deserialize(formatter.format(bossBarNotification.message()));
        BossBar bossBar = bossBarNotification.create(name);

        float progress = bossBarNotification.progress();
        Duration combatDuration = this.pluginConfig.settings.combatDuration;

        return new FightBossBar(audience, bossBar, progress, combatDuration);
    }
}
