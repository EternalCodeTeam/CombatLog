package com.eternalcode.combat.fight.drop;

import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public record DropResult(
    List<ItemStack> droppedItems,
    List<ItemStack> removedItems,
    int droppedExp
) {

    @Override
    public List<ItemStack> droppedItems() {
        return Collections.unmodifiableList(this.droppedItems);
    }

    @Override
    public List<ItemStack> removedItems() {
        return Collections.unmodifiableList(this.removedItems);
    }

}
