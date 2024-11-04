package dev.cryptics.unearth.common.blocks;

import dev.cryptics.unearth.common.blocks.entity.StampBlockEntity;
import dev.cryptics.unearth.compat.PastelCompat;
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
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class StampBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.values());

    public StampBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
        );
    }

    @NotNull
    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (stack.getItem() instanceof DyeItem dyeItem) {
            if (level.getBlockEntity(pos) instanceof StampBlockEntity stamp) {
                if (stamp.getColor() == dyeItem.getDyeColor().getTextColor()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                stamp.setColor(dyeItem.getDyeColor().getTextColor());
                if (!player.isCreative()) stack.setCount(stack.getCount() - 1);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                level.gameEvent(GameEvent.BLOCK_CHANGE, stamp.getBlockPos(), GameEvent.Context.of(player, stamp.getBlockState()));
                level.playSound(null, stamp.getBlockPos(), SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                return ItemInteractionResult.SUCCESS;
            }
        }
        Optional<ItemInteractionResult> result = PastelCompat.setStampPastelColor(hitResult, state, (StampBlockEntity) level.getBlockEntity(pos), player, stack, level, pos);
        if (result.isPresent()) return result.get();

        if (stack.getItem() instanceof GlowInkSacItem) {
            if (level.getBlockEntity(pos) instanceof StampBlockEntity stamp) {
                if (stamp.isLuminous()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                stamp.setLuminous(true);
                if (!player.isCreative()) stack.setCount(stack.getCount() - 1);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                level.gameEvent(GameEvent.BLOCK_CHANGE, stamp.getBlockPos(), GameEvent.Context.of(player, stamp.getBlockState()));
                level.playSound(null, stamp.getBlockPos(), SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                return ItemInteractionResult.SUCCESS;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @NotNull
    @Override
    public VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case UP -> Block.box(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
            case DOWN -> Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
            case NORTH -> Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
            case SOUTH -> Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
            case WEST -> Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
            case EAST -> Block.box(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
        };
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos attachedPos = pos.relative(facing.getOpposite());
        return level.getBlockState(attachedPos).isFaceSturdy(level, attachedPos, facing);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clickedFace = context.getClickedFace();
        BlockState state = this.defaultBlockState().setValue(FACING, clickedFace);
        return state.canSurvive(context.getLevel(), context.getClickedPos()) ? state : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new StampBlockEntity(blockPos, blockState);
    }

    @Override
    public boolean isPossibleToRespawnInThis(@NotNull BlockState state) {
        return true;
    }
}
