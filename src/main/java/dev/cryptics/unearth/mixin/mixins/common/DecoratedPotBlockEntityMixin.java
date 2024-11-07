package dev.cryptics.unearth.mixin.mixins.common;

import com.llamalad7.mixinextras.sugar.Local;
import dev.cryptics.unearth.common.blocks.entity.data.DecoratedPotColorLuminousData;
import dev.cryptics.unearth.mixin.ducks.IDecoratedPotBlockEntity;
import dev.cryptics.unearth.registry.common.UnearthDataComponentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.PotDecorations;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(DecoratedPotBlockEntity.class)
public abstract class DecoratedPotBlockEntityMixin extends BlockEntity implements IDecoratedPotBlockEntity {
    @Unique
    private DecoratedPotColorLuminousData colorLuminousData;

    public DecoratedPotBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void unearth$init(BlockPos pos, BlockState state, CallbackInfo ci) {
        this.colorLuminousData = DecoratedPotColorLuminousData.EMPTY.copy();
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    public void unearth$saveAdditional(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
        this.colorLuminousData.save(tag);
    }

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    public void unearth$loadAdditional(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
        this.colorLuminousData = DecoratedPotColorLuminousData.load(tag);
    }

    @Inject(method = "collectImplicitComponents", at = @At("TAIL"))
    protected void collectImplicitComponents(DataComponentMap.Builder components, CallbackInfo ci) {
        components.set(UnearthDataComponentTypes.POT_COLORS.get(), this.colorLuminousData);
    }

    @Inject(method = "applyImplicitComponents", at = @At("TAIL"))
    public void applyImplicitComponents(@NotNull DataComponentInput componentInput, CallbackInfo ci) {
        colorLuminousData = componentInput.getOrDefault(UnearthDataComponentTypes.POT_COLORS.get(), DecoratedPotColorLuminousData.EMPTY.copy());
    }

    @Inject(method = "removeComponentsFromTag", at = @At("TAIL"))
    public void removeComponentsFromTag(CompoundTag tag, CallbackInfo ci) {
        tag.remove("unearth");
    }

    /**
     * @author Unearth
     * @reason Overwrite to include custom data
     */
    @Overwrite
    @NotNull
    public CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void setColor(Direction direction, int color) {
        this.colorLuminousData.get(direction).setColor(color);
        this.markUpdated();
    }

    @Override
    public void setLuminous(Direction direction, boolean luminous) {
        this.colorLuminousData.get(direction).setLuminous(luminous);
        this.markUpdated();
    }

    @Override
    public DecoratedPotColorLuminousData getColorLuminousData() {
        return this.colorLuminousData;
    }

    @Override
    public void markUpdated() {
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
        }
    }
}
