package com.eternalcode.combat.fight.drop;

import java.util.List;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

public interface DropKeepInventoryService {

    List<ItemStack> nextItems(UUID uuid);

    boolean hasItems(UUID uuid);

    void addItems(UUID uuid, List<ItemStack> item);

    void addItem(UUID uuid, ItemStack item);
}
