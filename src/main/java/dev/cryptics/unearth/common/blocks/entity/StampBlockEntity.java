package dev.cryptics.unearth.common.blocks.entity;

import dev.cryptics.unearth.Unearth;
import dev.cryptics.unearth.registry.common.UnearthBlockEntitites;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StampBlockEntity extends BlockEntity {
    private Integer color = 0;
    private Item sherdItem = Items.ANGLER_POTTERY_SHERD;
    private boolean luminous;

    public StampBlockEntity(BlockPos pos, BlockState state) {
        super(UnearthBlockEntitites.STAMP_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        this.color = tag.getInt("color");
        this.luminous = tag.getBoolean("luminous");
        this.sherdItem = BuiltInRegistries.ITEM.get(ResourceLocation.parse(tag.getString("sherd_item")));
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("color", this.color);
        tag.putBoolean("luminous", this.luminous);
        tag.putString("sherd_item", BuiltInRegistries.ITEM.getKey(this.sherdItem).toString());
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        Unearth.LOGGER.info("StampBlockEntity.getUpdateTag: " + tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void markUpdated() {
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
        }
    }

    public void setColor(int color) {
        this.color = color;
        this.markUpdated();
    }

    public void setLuminous(boolean luminous) {
        this.luminous = luminous;
        this.markUpdated();
    }

    public int getColor() {
        return this.color;
    }

    public boolean isLuminous() {
        return this.luminous;
    }

    public void setSherdItem(Item sherdItem) {
        this.sherdItem = sherdItem;
        this.markUpdated();
    }

    public Item getSherdItem() {
        return this.sherdItem;
    }

}
