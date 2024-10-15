package dev.cryptics.archaeology.common.blocks.entity;

import dev.cryptics.archaeology.registry.common.ArchaeologyBlockEntitites;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class StampBlockEntity extends BlockEntity {
    private DyeColor color = DyeColor.WHITE;

    public StampBlockEntity(BlockPos pos, BlockState state) {
        super(ArchaeologyBlockEntitites.STAMP_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        this.color = DyeColor.byId(tag.getInt("color"));
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("color", this.color.getId());
    }

    private void markUpdated() {
        this.setChanged();
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
    }

    public void setColor(DyeColor color) {
        this.color = color;
        this.markUpdated();
    }

    public DyeColor getColor() {
        if (this.color == null) {
            return DyeColor.WHITE;
        }
        return this.color;
    }

}
