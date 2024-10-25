package dev.cryptics.unearth.common.blocks.entity;

import dev.cryptics.unearth.registry.common.UnearthBlockEntitites;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class StampBlockEntity extends BlockEntity {
    private DyeColor color = DyeColor.WHITE;
    private boolean luminous;

    public StampBlockEntity(BlockPos pos, BlockState state) {
        super(UnearthBlockEntitites.STAMP_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        this.color = DyeColor.byId(tag.getInt("color"));
        this.luminous = tag.getBoolean("luminous");
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("color", this.color.getId());
        tag.putBoolean("luminous", this.luminous);
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
    }

    private void markUpdated() {
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
        }
    }

    public void setColor(DyeColor color) {
        this.color = color;
        this.markUpdated();
    }

    public void setLuminous(boolean luminous) {
        this.luminous = luminous;
        this.markUpdated();
    }

    public DyeColor getColor() {
        if (this.color == null) {
            return DyeColor.WHITE;
        }
        return this.color;
    }

    public boolean isLuminous() {
        return this.luminous;
    }

}
