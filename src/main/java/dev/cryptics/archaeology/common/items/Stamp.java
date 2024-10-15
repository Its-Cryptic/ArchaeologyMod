package dev.cryptics.archaeology.common.items;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import dev.cryptics.archaeology.common.blocks.StampBlock;
import dev.cryptics.archaeology.common.blocks.entity.StampBlockEntity;
import dev.cryptics.archaeology.registry.common.ArchaeologyBlocks;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static net.minecraft.world.item.Items.*;

public class Stamp extends Item {
    private static final List<Item> ALL_SHERDS = List.of(ANGLER_POTTERY_SHERD, ARCHER_POTTERY_SHERD, ARMS_UP_POTTERY_SHERD, BLADE_POTTERY_SHERD, BREWER_POTTERY_SHERD, BURN_POTTERY_SHERD);
    private static final Map<Item, Block> SHERD_STAMP_MAP = ImmutableMap.of(
            ANGLER_POTTERY_SHERD, Blocks.STONE,
            ARCHER_POTTERY_SHERD, Blocks.DIRT,
            ARMS_UP_POTTERY_SHERD, Blocks.GRASS_BLOCK,
            BLADE_POTTERY_SHERD, Blocks.COBBLESTONE,
            BREWER_POTTERY_SHERD, Blocks.OAK_PLANKS,
            BURN_POTTERY_SHERD, Blocks.SAND
    );
    // Create a codec that
    private static final CompoundTag defaultTag = new CompoundTag() {{
        Item entry = SHERD_STAMP_MAP.keySet().stream().findFirst().orElse(ANGLER_POTTERY_SHERD);
        putString("current_stamp", entry.getName(entry.getDefaultInstance()).getString());
    }};
    private static final Properties properties = new Properties()
            .rarity(Rarity.UNCOMMON)
            .component(DataComponents.CUSTOM_DATA, CustomData.of(defaultTag))
            .component(DataComponents.CUSTOM_MODEL_DATA, CustomModelData.DEFAULT)
            .stacksTo(1);

    public Stamp() {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Block block = level.getBlockState(context.getClickedPos()).getBlock();
        Direction direction = context.getClickedFace();
        ItemStack offhandItemStack = Objects.requireNonNull(context.getPlayer()).getItemInHand(InteractionHand.OFF_HAND);
        if (offhandItemStack.getItem() instanceof DyeItem dyeItem) {

            ItemStack stack = context.getItemInHand();
            CustomModelData data = stack.get(DataComponents.CUSTOM_MODEL_DATA);
            if (data != null) {
                int val = data.value();
                int newVal = val == 1 ? 0 : 1;
                stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(newVal));
            }
            boolean isSolid = block.defaultBlockState().isSolidRender(level, context.getClickedPos());
            if (isSolid) {
                Block sherdBlock = SHERD_STAMP_MAP.getOrDefault(stack.getItem(), ArchaeologyBlocks.STAMP_BLOCK.get());
                BlockPos pos = context.getClickedPos().relative(direction);
                level.setBlock(pos, sherdBlock.defaultBlockState().setValue(StampBlock.FACING, direction.getOpposite()), 3);
                if (level.getBlockEntity(pos) instanceof StampBlockEntity blockEntity) {
                    blockEntity.setColor(dyeItem.getDyeColor());
                    level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(context.getPlayer(), blockEntity.getBlockState()));
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.literal("Shift is down"));
            if (stack.get(DataComponents.CUSTOM_DATA) != null) {
                tooltipComponents.add(Component.literal("Data: " + stack.get(DataComponents.CUSTOM_DATA)));
            }
            CustomModelData data = stack.get(DataComponents.CUSTOM_MODEL_DATA);
            if (data != null) {
                tooltipComponents.add(Component.literal("Model Data: " + stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(1))));
            }
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    private boolean isDye(Item item) {
        return item instanceof DyeItem;
    }
}
