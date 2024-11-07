package dev.cryptics.unearth.compat;

import com.mynamesraph.pastelpalettes.item.PastelDyeItem;
import dev.cryptics.unearth.common.blocks.StampBlock;
import dev.cryptics.unearth.common.blocks.entity.DecoratedPotBlockUtils;
import dev.cryptics.unearth.common.blocks.entity.StampBlockEntity;
import dev.cryptics.unearth.registry.common.UnearthBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PastelCompat {
    public static boolean LOADED;

    public static @Nullable ItemInteractionResult setPotPastelColor(BlockHitResult hitResult, BlockState blockState, DecoratedPotBlockEntity decoratedPotBlockEntity, Player player, ItemStack itemStack, Level level, BlockPos blockPos) {
        if (LOADED) {
            if (itemStack.getItem() instanceof PastelDyeItem pastelDyeItem) {
                int packedColor = pastelDyeItem.getDyeColor().getTextColor();
                return DecoratedPotBlockUtils.setColor(packedColor, hitResult, blockState, decoratedPotBlockEntity, player, itemStack, level, blockPos);
            }
        }
        return null;
    }

    public static Optional<ItemInteractionResult> setStampPastelColor(BlockHitResult hitResult, BlockState blockState, StampBlockEntity stampBlockEntity, Player player, ItemStack itemStack, Level level, BlockPos blockPos) {
        if (LOADED) {
            if (itemStack.getItem() instanceof PastelDyeItem dyeItem) {
                if (level.getBlockEntity(blockPos) instanceof StampBlockEntity stamp) {
                    if (stamp.getColor() == dyeItem.getDyeColor().getTextColor()) return Optional.of(ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION);
                    stamp.setColor(dyeItem.getDyeColor().getTextColor());
                    if (!player.isCreative()) itemStack.setCount(itemStack.getCount() - 1);
                    player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                    level.gameEvent(GameEvent.BLOCK_CHANGE, stamp.getBlockPos(), GameEvent.Context.of(player, stamp.getBlockState()));
                    level.playSound(null, stamp.getBlockPos(), SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return Optional.of(ItemInteractionResult.SUCCESS);
                }
            }
        }
        return Optional.empty();
    }

    public static void setStampPastelColor(Item offhandItem, Item sherdItem, Direction direction, Player player, Level level, BlockPos pos) {
        if (LOADED) {
            if (offhandItem instanceof PastelDyeItem dyeItem) {
                Block stampBlock = UnearthBlocks.STAMP_BLOCK.get();
                level.setBlock(pos, stampBlock.defaultBlockState().setValue(StampBlock.FACING, direction.getOpposite()), 3);
                if (level.getBlockEntity(pos) instanceof StampBlockEntity blockEntity) {
                    blockEntity.setColor(dyeItem.getDyeColor().getTextColor());
                    blockEntity.setSherdItem(sherdItem);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockEntity.getBlockState()));
                }
            }
        }
    }

    public static void init() {
        LOADED = ModList.get().isLoaded("past_el_palettes");
    }
}
