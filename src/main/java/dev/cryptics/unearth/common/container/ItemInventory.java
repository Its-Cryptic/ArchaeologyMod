package dev.cryptics.unearth.common.container;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jetbrains.annotations.NotNull;

/**
 * A container that is attached to an item stack. Ex: A backpack item
 */
public class ItemInventory implements Container {
    private final ItemStack stack;
    private final NonNullList<ItemStack> items = NonNullList.withSize(24, ItemStack.EMPTY);

    public ItemInventory(ItemStack stack) {
        this.stack = stack;
        ItemContainerContents contents = stack.get(DataComponents.CONTAINER);
        if (contents != null) {
            contents.copyInto(items);
        }
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @NotNull
    @Override
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @NotNull
    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack item = this.getItem(slot);
        ItemStack newItem = item.split(amount);
        if (item.isEmpty()) {
            this.items.set(slot, ItemStack.EMPTY);
        }
        this.setChanged();
        return newItem;
    }

    @NotNull
    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack item = this.getItem(slot);
        this.items.set(slot, ItemStack.EMPTY);
        return item;
    }

    public ItemStack addItem(ItemStack item){
        for(int i = 0; i < this.items.size(); i++){
            if(this.items.get(i).isEmpty()){
                this.items.set(i, item);
                this.setChanged();
                return ItemStack.EMPTY;
            } else{
                ItemStack itemStack = this.items.get(i);
                if(ItemStack.isSameItemSameComponents(item, itemStack)){
                    int j = 64 - itemStack.getCount();
                    if(item.getCount() <= j){
                        ItemStack item2 = itemStack.copy();
                        item2.grow(item.getCount());
                        this.items.set(i, item2);
                        this.setChanged();
                        return ItemStack.EMPTY;
                    } else{
                        itemStack.grow(j);
                        item.shrink(j);
                        this.setChanged();
                    }
                }
            }
        }
        return item;
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack itemStack) {
        this.items.set(slot, itemStack);
        this.setChanged();
    }

    @Override
    public void setChanged() {
        this.stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.items));
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.items.clear();
        this.setChanged();
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }
}
