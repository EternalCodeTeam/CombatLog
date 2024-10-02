package com.eternalcode.combat.fight.controller;

import com.eternalcode.combat.fight.FightManager;
import com.eternalcode.combat.fight.event.CauseOfUnTag;
import com.eternalcode.combat.config.implementation.PluginConfig;
import com.eternalcode.combat.fight.logout.LogoutService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class FightUnTagController implements Listener {

    private final FightManager fightManager;
    private final PluginConfig config;
    private final LogoutService logoutService;

    public FightUnTagController(FightManager fightManager, PluginConfig config, LogoutService logoutService) {
        this.fightManager = fightManager;
        this.config = config;
        this.logoutService = logoutService;
    }

    @EventHandler
    void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (!this.fightManager.isInCombat(player.getUniqueId())) {
            return;
        }

        CauseOfUnTag cause = this.getDeathCause(player, killer);

        this.fightManager.untag(player.getUniqueId(), cause);

        if (killer != null && this.config.settings.shouldReleaseAttacker) {
            this.fightManager.untag(killer.getUniqueId(), CauseOfUnTag.ATTACKER_RELEASE);
        }
    }

    private CauseOfUnTag getDeathCause(Player player, Player killer) {
        if (this.logoutService.hasLoggedOut(player.getUniqueId())) {
            return CauseOfUnTag.LOGOUT;
        }

        if (killer == null) {
            return CauseOfUnTag.DEATH;
        }

        if (this.fightManager.isInCombat(killer.getUniqueId())) {
            return CauseOfUnTag.DEATH_BY_PLAYER;
        }

        return CauseOfUnTag.DEATH;
    }

}
