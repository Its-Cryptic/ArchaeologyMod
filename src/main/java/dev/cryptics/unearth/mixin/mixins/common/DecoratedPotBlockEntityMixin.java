package dev.cryptics.unearth.mixin.mixins.common;

import dev.cryptics.unearth.Unearth;
import dev.cryptics.unearth.mixin.ducks.IDecoratedPotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(DecoratedPotBlockEntity.class)
public abstract class DecoratedPotBlockEntityMixin extends BlockEntity implements IDecoratedPotBlockEntity {
    @Unique
    private Map<Direction, DyeColor> dyeColorMap = new HashMap<>();
    @Unique
    private Map<Direction, Boolean> luminousMap = new HashMap<>();
    @Unique
    private CustomData customData = CustomData.EMPTY;

    public DecoratedPotBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Unique
    public CompoundTag saveColorLuminousData() {
        CompoundTag tag = new CompoundTag();
        for (Direction direction : dyeColorMap.keySet()) {
            DyeColor color = this.dyeColorMap.get(direction);
            if (color != null) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putInt("color", this.getId(color));
                compoundTag.putBoolean("luminous", this.luminousMap.getOrDefault(direction, false));
                tag.put(direction.getSerializedName(), compoundTag);
            }
        }
        return tag;
    }

    @Unique
    public void loadColorLuminousData(CompoundTag tag) {
        for (Direction direction : Direction.values()) {
            if (tag.contains(direction.getSerializedName())) {
                CompoundTag compoundTag = tag.getCompound(direction.getSerializedName());
                this.dyeColorMap.put(direction, this.getDyeColor(compoundTag.getInt("color")));
                this.luminousMap.put(direction, compoundTag.getBoolean("luminous"));
            }
        }
    }

    public CompoundTag saveCustomData(CompoundTag tag) {
        if (this.customData.equals(CustomData.EMPTY)) {
            return tag;
        } else {
            tag.put("custom", CustomData.CODEC.encodeStart(NbtOps.INSTANCE, customData).getOrThrow());
            return tag;
        }
    }

    private CustomData load(@Nullable CompoundTag tag) {
        return tag != null && tag.contains("custom") ? (CustomData) CustomData.CODEC.parse(NbtOps.INSTANCE, tag.get("custom")).result().orElse(CustomData.EMPTY) : CustomData.EMPTY;
    }


    @Inject(method = "saveAdditional", at = @At("TAIL"))
    public void unearth$saveAdditional(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
        CompoundTag unearthData = new CompoundTag();
        unearthData.put("colors", this.saveColorLuminousData());
        tag.put("unearth", unearthData);
        this.saveCustomData(tag);
    }

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    public void unearth$loadAdditional(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
        CompoundTag unearthData = tag.getCompound("unearth");
        this.loadColorLuminousData(unearthData.getCompound("colors"));
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag, registries);
        return tag;
    }

//    @Inject(method = "getUpdateTag", at = @At("TAIL"))
//    public void unearth$getUpdateTag(HolderLookup.Provider registries, CallbackInfo ci) {
//        CompoundTag tag = new CompoundTag();
//        this.saveAdditional(tag, registries);
//    }


    @Unique
    @Nullable
    private DyeColor getDyeColor(int id) {
        return id == -1 ? null : DyeColor.byId(id);
    }


    private int getId(DyeColor color) {
        return color == null ? -1 : color.getId();
    }

    @Override
    public void setColor(Direction direction, DyeColor color) {
        this.dyeColorMap.put(direction.getOpposite(), color);
        this.markUpdated();
    }

    @Override
    public void setLuminous(Direction direction, boolean luminous) {
        this.luminousMap.put(direction.getOpposite(), luminous);
        this.markUpdated();
    }

    @Override
    public Map<Direction, DyeColor> getColorsMap() {
        return this.dyeColorMap;
    }

    @Override
    public Map<Direction, Boolean> getLuminousMap() {
        return this.luminousMap;
    }

    @Override
    public void markUpdated() {
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
        }
    }
}
