package com.eternalcode.combat.fight.drop;

import com.eternalcode.combat.fight.FightManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.UUID;

public class DropController implements Listener {

    private final DropService dropService;
    private final DropKeepInventoryService keepInventoryManager;
    private final DropSettings dropSettings;
    private final FightManager fightManager;

    public DropController(DropService dropService, DropKeepInventoryService keepInventoryManager, DropSettings dropSettings, FightManager fightManager) {
        this.dropService = dropService;
        this.keepInventoryManager = keepInventoryManager;
        this.dropSettings = dropSettings;
        this.fightManager = fightManager;
    }

    @EventHandler(priority = EventPriority.LOW)
    void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        DropType dropType = this.dropSettings.dropType;

        if (dropType == DropType.UNCHANGED || !this.fightManager.isInCombat(player.getUniqueId())) {
            return;
        }
        List<ItemStack> drops = event.getDrops();

        Drop drop = Drop.builder()
            .player(player)
            .killer(player.getKiller())
            .droppedItems(drops)
            .droppedExp(player.getTotalExperience())
            .build();

        DropResult result = this.dropService.modify(dropType, drop);

        if (result == null) {
            return;
        }

        drops.clear();
        drops.addAll(result.droppedItems());

        this.keepInventoryManager.addItems(player.getUniqueId(), result.removedItems());

        if (this.dropSettings.affectExperience) {
            event.setDroppedExp(drop.getDroppedExp());
        }
    }

    @EventHandler
    void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID playerUniqueId = player.getUniqueId();

        if (this.keepInventoryManager.hasItems(playerUniqueId)) {
            PlayerInventory playerInventory = player.getInventory();

            ItemStack[] itemsToGive = this.keepInventoryManager.nextItems(playerUniqueId)
                .toArray(new ItemStack[0]);

            playerInventory.addItem(itemsToGive);
        }
    }

}
