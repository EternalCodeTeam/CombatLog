package com.eternalcode.combat.fight;

import com.eternalcode.combat.config.implementation.PluginConfig;
import com.eternalcode.combat.fight.bossbar.FightBossBarService;
import com.eternalcode.combat.fight.event.CauseOfUnTag;
import com.eternalcode.combat.notification.Notification;
import com.eternalcode.combat.notification.NotificationAnnouncer;
import com.eternalcode.combat.notification.implementation.BossBarNotification;
import com.eternalcode.combat.util.DurationUtil;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import panda.utilities.text.Formatter;

import java.time.Duration;
import java.util.UUID;

public class FightTask implements Runnable {

    private final Server server;
    private final PluginConfig config;
    private final FightManager fightManager;
    private final FightBossBarService bossBarService;
    private final NotificationAnnouncer announcer;

    public FightTask(Server server, PluginConfig config, FightManager fightManager, FightBossBarService bossBarService, NotificationAnnouncer announcer) {
        this.server = server;
        this.config = config;
        this.fightManager = fightManager;
        this.bossBarService = bossBarService;
        this.announcer = announcer;
    }

    @Override
    public void run() {
        for (FightTag fightTag : this.fightManager.getFights()) {
            Player player = this.server.getPlayer(fightTag.getTaggedPlayer());

            if (player == null) {
                continue;
            }

            UUID playerUniqueId = player.getUniqueId();

            if (fightTag.isExpired()) {
                this.fightManager.untag(playerUniqueId, CauseOfUnTag.TIME_EXPIRED);
                return;
            }

            Duration remaining = fightTag.getRemainingDuration();
            Formatter formatter = new Formatter()
                    .register("{TIME}", DurationUtil.format(remaining));

            Notification combatNotification = this.config.messages.combatNotification;

            this.sendFightNotification(player, fightTag, combatNotification, formatter);
        }
    }

    private void sendFightNotification(Player player, FightTag fightTag, Notification notification, Formatter formatter) {
        if (notification instanceof BossBarNotification bossBarNotification) {
            this.bossBarService.send(player, fightTag, bossBarNotification, formatter);
            return;
        }

        this.announcer.send(player, notification, formatter);
    }
}
