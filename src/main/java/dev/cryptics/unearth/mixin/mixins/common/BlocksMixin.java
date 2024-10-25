package dev.cryptics.unearth.mixin.mixins.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchflowerCropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public class BlocksMixin {
    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;of()Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(value = "CONSTANT", args = "stringValue=torchflower")
            )
    )
    private static BlockBehaviour.Properties unearth$addPropertiesToTorchFlower(BlockBehaviour.Properties original) {
        return original.lightLevel(blocksState -> 10);
    }

    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;of()Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(value = "CONSTANT", args = "stringValue=torchflower_crop")
            )
    )
    private static BlockBehaviour.Properties unearth$addPropertiesToTorchFlowerCrop(BlockBehaviour.Properties original) {
        return original.lightLevel(blocksState -> blocksState.getValue(TorchflowerCropBlock.AGE) == 0 ? 3 : 6);
    }
}