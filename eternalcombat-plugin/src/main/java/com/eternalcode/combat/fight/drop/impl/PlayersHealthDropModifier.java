package com.eternalcode.combat.fight.drop.impl;

import com.eternalcode.combat.fight.drop.Drop;
import com.eternalcode.combat.fight.drop.DropModifier;
import com.eternalcode.combat.fight.drop.DropResult;
import com.eternalcode.combat.fight.drop.DropSettings;
import com.eternalcode.combat.fight.drop.DropType;
import com.eternalcode.combat.fight.logout.Logout;
import com.eternalcode.combat.fight.logout.LogoutService;
import com.eternalcode.combat.util.InventoryUtil;
import com.eternalcode.combat.util.MathUtil;
import com.eternalcode.combat.util.RemoveItemResult;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public class PlayersHealthDropModifier implements DropModifier {

    private final DropSettings settings;
    private final LogoutService logoutService;

    public PlayersHealthDropModifier(DropSettings settings, LogoutService logoutService) {
        this.settings = settings;
        this.logoutService = logoutService;
    }

    @Override
    public DropType getDropType() {
        return DropType.PLAYERS_HEALTH;
    }

    @Override
    public DropResult modifyDrop(Drop drop) {
        Optional<Logout> logoutOptional = this.logoutService.nextLogoutFor(drop.getPlayer().getUniqueId());

        if (logoutOptional.isEmpty()) {
            return null;
        }


        Logout logout = logoutOptional.get();
        Player player = drop.getPlayer();

        List<ItemStack> droppedItems = drop.getDroppedItems();

        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        double health = logout.health();

        int percentHealth = MathUtil.getRoundedCountPercentage(health, maxHealth);
        int reversedPercent = MathUtil.clamp(100 - percentHealth, this.settings.playersHealthPercentClamp, 100);

        int itemsToDelete = InventoryUtil.calculateItemsToDelete(reversedPercent, droppedItems, ItemStack::getAmount);
        int droppedExp = MathUtil.getRoundedCountFromPercentage(reversedPercent, drop.getDroppedExp());

        RemoveItemResult result = InventoryUtil.removeRandomItems(droppedItems, itemsToDelete);

        return new DropResult(result.restItems(), result.removedItems(), droppedExp);
    }
}
