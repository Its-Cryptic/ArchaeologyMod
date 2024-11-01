package dev.cryptics.unearth.common.container;

import dev.cryptics.unearth.common.items.StampItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ItemInventorySlot extends Slot {
    public ItemInventorySlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem().canFitInsideContainerItems() && StampItem.ALL_SHERDS.contains(stack.getItem());
    }
}
