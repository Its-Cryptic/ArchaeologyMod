package dev.cryptics.unearth.mixin.mixins.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TorchflowerCropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.common.util.TriState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TorchflowerCropBlock.class)
public abstract class TorchFlowerCropBlockMixin extends CropBlock {
    @Shadow
    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;

    public TorchFlowerCropBlockMixin(Properties properties) {
        super(properties);
    }

    private static final Properties properties = Properties.of().mapColor(MapColor.PLANT).lightLevel(blockState -> blockState.getValue(AGE) == 0 ? 2 : 7).noCollission().randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY);
    private static final String constructor = "<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V";

//    @ModifyVariable(method = constructor, at = @At("HEAD"), ordinal = 0)
//    private static Properties modifyProperties(Properties properties) {
//        return properties.lightLevel(blockState -> blockState.getValue(AGE) == 0 ? 3 : 6);
//    }

}
