package com.eternalcode.combat.fight.logout;

import com.eternalcode.combat.fight.FightManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LogoutController implements Listener {

    private final FightManager fightManager;
    private final LogoutService logoutManager;

    public LogoutController(FightManager fightManager, LogoutService logoutManager) {
        this.fightManager = fightManager;
        this.logoutManager = logoutManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (!this.fightManager.isInCombat(player.getUniqueId())) {
            return;
        }

        logoutManager.punishForLogout(player);
        player.setHealth(0.0);
    }

}
