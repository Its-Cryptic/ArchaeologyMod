package dev.cryptics.unearth.mixin.mixins.common;

import dev.cryptics.unearth.Unearth;
import dev.cryptics.unearth.mixin.ducks.IDecoratedPotBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
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
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;
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

    /**
     * @author
     * @reason
     */
    @Overwrite
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(blockPos) instanceof DecoratedPotBlockEntity decoratedpotblockentity) {
            if (level.isClientSide) {
                return ItemInteractionResult.CONSUME;
            } else {
                if (itemStack.getItem() instanceof DyeItem dyeItem) {
                    DyeColor dyeColor = dyeItem.getDyeColor();
                    Direction hitDirection = hitResult.getDirection();
                    Direction blockDirection = blockState.getValue(HORIZONTAL_FACING);
                    if (archaeologyMod$directionFunctionMap.containsKey(hitDirection)) {
                        IDecoratedPotBlockEntity blockEntity = (IDecoratedPotBlockEntity) (Object) decoratedpotblockentity;
                        Direction relativeDirection = archaeologyMod$directionFunctionMap.get(blockDirection).apply(hitDirection);
                        //if (blockEntity.getColorsMap().get(relativeDirection) == dyeColor) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                        blockEntity.setColor(relativeDirection, dyeColor);

                        if (!player.isCreative()) itemStack.setCount(itemStack.getCount() - 1);
                        player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                        level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, blockState));
                        level.playSound(null, blockPos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        return ItemInteractionResult.SUCCESS;
                    }
                }
                if (itemStack.getItem() instanceof GlowInkSacItem glowInkSacItem) {
                    Direction hitDirection = hitResult.getDirection();
                    Direction blockDirection = blockState.getValue(HORIZONTAL_FACING);
                    if (archaeologyMod$directionFunctionMap.containsKey(hitDirection)) {
                        IDecoratedPotBlockEntity blockEntity = (IDecoratedPotBlockEntity) (Object) decoratedpotblockentity;
                        Direction relativeDirection = archaeologyMod$directionFunctionMap.get(blockDirection).apply(hitDirection);
                        //if (blockEntity.getColorsMap().get(relativeDirection) == dyeColor) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                        blockEntity.setLuminous(relativeDirection, true);

                        if (!player.isCreative()) itemStack.setCount(itemStack.getCount() - 1);
                        player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                        level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, blockState));
                        level.playSound(null, blockPos, SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        return ItemInteractionResult.SUCCESS;
                    }
                }
                ItemStack itemstack1 = decoratedpotblockentity.getTheItem();
                if (!itemStack.isEmpty()
                        && (
                        itemstack1.isEmpty()
                                || ItemStack.isSameItemSameComponents(itemstack1, itemStack) && itemstack1.getCount() < itemstack1.getMaxStackSize()
                )) {
                    decoratedpotblockentity.wobble(DecoratedPotBlockEntity.WobbleStyle.POSITIVE);
                    player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                    ItemStack itemstack = itemStack.consumeAndReturn(1, player);
                    float f;
                    if (decoratedpotblockentity.isEmpty()) {
                        decoratedpotblockentity.setTheItem(itemstack);
                        f = (float)itemstack.getCount() / (float)itemstack.getMaxStackSize();
                    } else {
                        itemstack1.grow(1);
                        f = (float)itemstack1.getCount() / (float)itemstack1.getMaxStackSize();
                    }

                    level.playSound(null, blockPos, SoundEvents.DECORATED_POT_INSERT, SoundSource.BLOCKS, 1.0F, 0.7F + 0.5F * f);
                    if (level instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                ParticleTypes.DUST_PLUME,
                                (double)blockPos.getX() + 0.5,
                                (double)blockPos.getY() + 1.2,
                                (double)blockPos.getZ() + 0.5,
                                7,
                                0.0,
                                0.0,
                                0.0,
                                0.0
                        );
                    }

                    decoratedpotblockentity.setChanged();
                    level.gameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);
                    return ItemInteractionResult.SUCCESS;
                } else {
                    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                }
            }
        } else {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
    }
}
