package com.eternalcode.combat.util;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.ToIntFunction;

public class InventoryUtil {

    private static final Random RANDOM = new Random();

    private InventoryUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static <T> int calculateItemsToDelete(int percent, Collection<T> objectList, ToIntFunction<? super T> mapper) {
        return MathUtil.getRoundedCountFromPercentage(percent, MathUtil.sum(objectList, mapper));
    }

    public static RemoveItemResult removeRandomItems(List<ItemStack> list, int itemsToDelete) {
        List<ItemStack> currentItems = new ArrayList<>(list);
        List<ItemStack> removedItems = new ArrayList<>();

        int currentItemsToDelete = itemsToDelete;
        while (currentItemsToDelete > 0) {
            int randomIndex = RANDOM.nextInt(list.size());
            ItemStack currentItem = currentItems.get(randomIndex);

            int amount = currentItem.getAmount();
            int randomAmount = RANDOM.nextInt(0, Math.min(currentItemsToDelete, amount) + 1);

            if (amount <= 0 || randomAmount <= 0) {
                continue;
            }

            ItemStack removedItem = currentItem.clone();
            removedItem.setAmount(randomAmount);
            removedItems.add(removedItem);

            currentItem.setAmount(amount - randomAmount);

            currentItemsToDelete -= randomAmount;
        }

        return new RemoveItemResult(currentItems, removedItems);
    }

}
