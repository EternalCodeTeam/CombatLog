package com.eternalcode.combat.region;

import com.eternalcode.combat.config.implementation.PluginConfig;
import com.eternalcode.combat.fight.FightManager;
import com.eternalcode.combat.notification.NotificationAnnouncer;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

public class RegionController implements Listener {

    private final NotificationAnnouncer announcer;
    private final RegionProvider regionProvider;
    private final FightManager fightManager;
    private final PluginConfig pluginConfig;

    public RegionController(NotificationAnnouncer announcer, RegionProvider regionProvider, FightManager fightManager, PluginConfig pluginConfig) {
        this.announcer = announcer;
        this.regionProvider = regionProvider;
        this.fightManager = fightManager;
        this.pluginConfig = pluginConfig;
    }

    @EventHandler
    void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!this.fightManager.isInCombat(player.getUniqueId())) {
            return;
        }

        Location locationTo = event.getTo();
        int xTo = locationTo.getBlockX();
        int yTo = locationTo.getBlockY();
        int zTo = locationTo.getBlockZ();

        Location locationFrom = event.getFrom();
        int xFrom = locationFrom.getBlockX();
        int yFrom = locationFrom.getBlockY();
        int zFrom = locationFrom.getBlockZ();

        if (xTo != xFrom || yTo != yFrom || zTo != zFrom) {
            Optional<Region> regionOptional = this.regionProvider.getRegion(locationTo);
            if (regionOptional.isEmpty()) {
                return;
            }

            Region region = regionOptional.get();
            Location centerOfRegion = region.getCenter();
            Location subtract = player.getLocation().subtract(centerOfRegion);

            Vector knockbackVector = new Vector(subtract.getX(), 0, subtract.getZ()).normalize();
            Vector configuredVector = new Vector(
                this.pluginConfig.settings.blockedRegionKnockMultiplier,
                0.5,
                this.pluginConfig.settings.blockedRegionKnockMultiplier);

            player.setVelocity(knockbackVector.multiply(configuredVector));

            this.announcer.sendMessage(player, this.pluginConfig.messages.cantEnterOnRegion);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if (!this.fightManager.isInCombat(player.getUniqueId())) {
            return;
        }

        Location targetLocation = event.getTo();

        if (this.regionProvider.isInRegion(targetLocation)) {
            event.setCancelled(true);
            this.announcer.sendMessage(player, this.pluginConfig.messages.cantEnterOnRegion);
        }
    }
}
