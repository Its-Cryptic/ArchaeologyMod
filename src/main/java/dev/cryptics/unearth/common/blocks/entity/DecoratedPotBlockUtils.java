package dev.cryptics.unearth.common.blocks.entity;

import dev.cryptics.unearth.mixin.ducks.IDecoratedPotBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class DecoratedPotBlockUtils {

    private static final Map<Direction, UnaryOperator<Direction>> directionFunctionMap = Util.make(new HashMap<>(), map -> {
        map.put(Direction.NORTH, direction -> direction);
        map.put(Direction.WEST, Direction::getClockWise);
        map.put(Direction.SOUTH, Direction::getOpposite);
        map.put(Direction.EAST, Direction::getCounterClockWise);
    });

    public static ItemInteractionResult setColor(int packedColor, BlockHitResult hitResult, BlockState blockState, DecoratedPotBlockEntity decoratedPotBlockEntity, Player player, ItemStack itemStack, Level level, BlockPos blockPos) {
        Direction hitDirection = hitResult.getDirection();
        Direction blockDirection = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        if (directionFunctionMap.containsKey(hitDirection)) {
            Direction relativeDirection = directionFunctionMap.get(blockDirection).apply(hitDirection);
            IDecoratedPotBlockEntity blockEntity = (IDecoratedPotBlockEntity) (Object) decoratedPotBlockEntity;
            blockEntity.setColor(relativeDirection, packedColor);

            if (!player.isCreative()) itemStack.setCount(itemStack.getCount() - 1);
            player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
            level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, blockState));
            level.playSound(null, blockPos, SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
