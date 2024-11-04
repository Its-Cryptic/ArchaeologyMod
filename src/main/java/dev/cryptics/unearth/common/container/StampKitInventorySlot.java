package dev.cryptics.unearth.common.container;

import dev.cryptics.unearth.common.items.StampItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class StampKitInventorySlot extends Slot {
    public StampKitInventorySlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        boolean canFitInsideContainerItems = stack.getItem().canFitInsideContainerItems();
        boolean isSherd = StampItem.ALL_SHERDS.contains(stack.getItem());
        boolean isSpecificSherdSlot = this.getSlotIndex() == StampItem.ALL_SHERDS.indexOf(stack.getItem());
        if (this.getSlotIndex() == 23) {
            return canFitInsideContainerItems && isSherd;
        } else {
            return canFitInsideContainerItems && isSherd && isSpecificSherdSlot;
        }
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }
}
