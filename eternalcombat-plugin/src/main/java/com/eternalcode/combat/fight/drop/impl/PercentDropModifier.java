package com.eternalcode.combat.fight.drop.impl;

import com.eternalcode.combat.fight.drop.Drop;
import com.eternalcode.combat.fight.drop.DropModifier;
import com.eternalcode.combat.fight.drop.DropResult;
import com.eternalcode.combat.fight.drop.DropSettings;
import com.eternalcode.combat.fight.drop.DropType;
import com.eternalcode.combat.util.InventoryUtil;
import com.eternalcode.combat.util.MathUtil;
import com.eternalcode.combat.util.RemoveItemResult;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PercentDropModifier implements DropModifier {

    private final DropSettings settings;

    public PercentDropModifier(DropSettings settings) {
        this.settings = settings;
    }

    @Override
    public DropType getDropType() {
        return DropType.PERCENT;
    }

    @Override
    public DropResult modifyDrop(Drop drop) {
        int dropItemPercent = 100 - MathUtil.clamp(this.settings.dropItemPercent, 0, 100);
        List<ItemStack> droppedItems = drop.getDroppedItems();

        int itemsToDelete = InventoryUtil.calculateItemsToDelete(dropItemPercent, droppedItems, ItemStack::getAmount);
        int droppedExp = MathUtil.getRoundedCountFromPercentage(dropItemPercent, drop.getDroppedExp());

        RemoveItemResult result = InventoryUtil.removeRandomItems(droppedItems, itemsToDelete);

        return new DropResult(result.restItems(), result.removedItems(), droppedExp);
    }
}
