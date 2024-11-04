package dev.cryptics.unearth.client.screen;

import dev.cryptics.unearth.common.container.ItemInventorySlot;
import dev.cryptics.unearth.registry.common.UnearthMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StampKitMenu extends AbstractContainerMenu {
    public static final int CONTAINER_SIZE = 24;
    private final Container container;

    public StampKitMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(CONTAINER_SIZE));
    }
    public StampKitMenu(int containerId, Inventory playerInventory, Container container) {
        super(UnearthMenuTypes.STAMP_KIT_MENU.get(), containerId);
        checkContainerSize(container, CONTAINER_SIZE);
        this.container = container;

        int height = 2;
        int width = 9;

        // Container Slots
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                this.addSlot(new ItemInventorySlot(container, j + i * width, 8 + j * 18, 18 + i * 18));
            }
        }
        // Add 5 more slots right below the first 18 slots, this time centered
        for (int i = 0; i < 5; ++i) {
            this.addSlot(new ItemInventorySlot(container, i + 18, 26 + i * 18 + 18, 18 + 2 * 18));
        }
        // Add 1 more slot right below the 5 slots, this time centered
        this.addSlot(new ItemInventorySlot(container, 23, 26 + 2 * 18 + 18, 18 + 4 * 18));


        // Player Inventory Slots
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
            }
        }

        // Hotbar Slots
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 198));
        }
    }

    @NotNull
    @Override
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack item = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack item2 = slot.getItem();
            item = item2.copy();
            if (index < this.container.getContainerSize()) {
                if (!this.moveItemStackTo(item2, this.container.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(item2, 0, this.container.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (item2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return item;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.container.stillValid(player);
    }
}
