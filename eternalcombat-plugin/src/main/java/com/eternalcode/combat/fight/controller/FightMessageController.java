package com.eternalcode.combat.fight.controller;

import com.eternalcode.combat.config.implementation.PluginConfig;
import com.eternalcode.combat.fight.FightManager;
import com.eternalcode.combat.fight.bossbar.FightBossBarService;
import com.eternalcode.combat.fight.event.FightTagEvent;
import com.eternalcode.combat.fight.event.FightUntagEvent;
import com.eternalcode.combat.notification.NotificationAnnouncer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class FightMessageController implements Listener {

    private final FightManager fightManager;
    private final NotificationAnnouncer announcer;
    private final FightBossBarService bossBarService;
    private final PluginConfig config;
    private final Server server;

    public FightMessageController(FightManager fightManager, NotificationAnnouncer announcer, FightBossBarService bossBarService, PluginConfig config, Server server) {
        this.fightManager = fightManager;
        this.announcer = announcer;
        this.bossBarService = bossBarService;
        this.config = config;
        this.server = server;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    void onTag(FightTagEvent event) {
        Player player = this.server.getPlayer(event.getPlayer());

        if (player == null) {
            throw new IllegalStateException("Player cannot be null!");
        }

        if (this.fightManager.isInCombat(player.getUniqueId())) {
            return;
        }

        this.announcer.sendMessage(player, this.config.messages.playerTagged);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    void onUnTag(FightUntagEvent event) {
        Player player = this.server.getPlayer(event.getPlayer());

        if (player == null) {
            throw new IllegalStateException("Player cannot be null!");
        }

        this.announcer.sendMessage(player, this.config.messages.playerUntagged);
        this.bossBarService.hide(event.getPlayer());
    }
}
