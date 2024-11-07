package dev.cryptics.unearth.mixin.mixins.common;

import com.llamalad7.mixinextras.sugar.Local;
import dev.cryptics.unearth.common.blocks.entity.DecoratedPotBlockUtils;
import dev.cryptics.unearth.compat.PastelCompat;
import dev.cryptics.unearth.mixin.ducks.IDecoratedPotBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.GlowInkSacItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@Mixin(DecoratedPotBlock.class)
public abstract class DecoratedPotBlockMixin {

    @Shadow
    private static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    @Unique
    private static final Map<Direction, UnaryOperator<Direction>> archaeologyMod$directionFunctionMap = Util.make(new HashMap<>(), map -> {
        map.put(Direction.NORTH, direction -> direction);
        map.put(Direction.WEST, Direction::getClockWise);
        map.put(Direction.SOUTH, Direction::getOpposite);
        map.put(Direction.EAST, Direction::getCounterClockWise);
    });

    @Inject(method = "useItemOn",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/DecoratedPotBlockEntity;getTheItem()Lnet/minecraft/world/item/ItemStack;"
            ),
            cancellable = true
    )
    public void unearth$useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<ItemInteractionResult> cir, @Local DecoratedPotBlockEntity decoratedpotblockentity) {
        if (itemStack.getItem() instanceof DyeItem dyeItem) {
            int packedColor = dyeItem.getDyeColor().getTextColor();
            ItemInteractionResult result = DecoratedPotBlockUtils.setColor(packedColor, hitResult, blockState, decoratedpotblockentity, player, itemStack, level, blockPos);
            cir.setReturnValue(result);
            return;
        }

        ItemInteractionResult result = PastelCompat.setPotPastelColor(hitResult, blockState, decoratedpotblockentity, player, itemStack, level, blockPos);
        if (result != null) {
            cir.setReturnValue(result);
            return;
        }

        if (itemStack.getItem() instanceof GlowInkSacItem) {
            Direction hitDirection = hitResult.getDirection();
            Direction blockDirection = blockState.getValue(HORIZONTAL_FACING);
            if (archaeologyMod$directionFunctionMap.containsKey(hitDirection)) {
                IDecoratedPotBlockEntity blockEntity = (IDecoratedPotBlockEntity) (Object) decoratedpotblockentity;
                Direction relativeDirection = archaeologyMod$directionFunctionMap.get(blockDirection).apply(hitDirection);

                if (blockEntity.getLuminousMap().getOrDefault(relativeDirection.getOpposite(), false)) {
                    cir.setReturnValue(ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION);
                    return;
                }

                blockEntity.setLuminous(relativeDirection, true);
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                    player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                }
                level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, blockState));
                level.playSound(null, blockPos, SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                cir.setReturnValue(ItemInteractionResult.SUCCESS);
            }
        }
    }

}
