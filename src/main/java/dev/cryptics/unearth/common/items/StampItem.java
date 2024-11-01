package dev.cryptics.unearth.common.items;

import com.google.common.collect.ImmutableMap;
import dev.cryptics.unearth.Unearth;
import dev.cryptics.unearth.client.render.blockentity.CustomDecoratedPotRenderer;
import dev.cryptics.unearth.client.screen.StampKitMenu;
import dev.cryptics.unearth.common.container.ItemInventory;
import dev.cryptics.unearth.common.blocks.StampBlock;
import dev.cryptics.unearth.common.blocks.entity.StampBlockEntity;
import dev.cryptics.unearth.registry.common.UnearthBlocks;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static net.minecraft.world.item.Items.*;

public class StampItem extends Item {
    private static final Properties properties = new Properties()
            .rarity(Rarity.UNCOMMON)
            .component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
            .component(DataComponents.CUSTOM_MODEL_DATA, CustomModelData.DEFAULT)
            .stacksTo(1);

    public StampItem() {
        super(properties);
    }

    /**
     * Ignore how bad this code is, I was in a rush
     * @param context
     * @return
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemContainerContents contents = context.getItemInHand().get(DataComponents.CONTAINER);
        if (contents == null) {
            return InteractionResult.FAIL;
        }

        ItemStack sherdItem = contents.getStackInSlot(23);
        if (sherdItem.isEmpty()) {
            return InteractionResult.FAIL;
        }
        if (!ALL_SHERDS.contains(sherdItem.getItem())) {
            return InteractionResult.FAIL;
        }
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
                Block sherdBlock = UnearthBlocks.STAMP_BLOCK.get();
                BlockPos pos = context.getClickedPos().relative(direction);
                level.setBlock(pos, sherdBlock.defaultBlockState().setValue(StampBlock.FACING, direction.getOpposite()), 3);
                if (level.getBlockEntity(pos) instanceof StampBlockEntity blockEntity) {
                    blockEntity.setColor(dyeItem.getDyeColor());
                    blockEntity.setSherdItem(sherdItem.getItem());
                    level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(context.getPlayer(), blockEntity.getBlockState()));
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack stampItem = player.getItemInHand(usedHand);
        if(player.isShiftKeyDown()){
            openMenu(player, level, stampItem);
        }
        return InteractionResultHolder.sidedSuccess(stampItem, level.isClientSide);
    }

    public static ItemInventory getInventory(ItemStack stack) {
        return new ItemInventory(stack);
    }

    @Override
    public boolean canFitInsideContainerItems() {
        NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
        ItemContainerContents container = this.components().get(DataComponents.CONTAINER);
        container.copyInto(items);
        return !(container.stream().toList().isEmpty()) && super.canFitInsideContainerItems();
    }

    public void openMenu(Player player, Level level, ItemStack item){
        if(!level.isClientSide()){
            player.level().playSound(null, player.getOnPos(), SoundEvents.ARMOR_EQUIP_LEATHER.value(), SoundSource.BLOCKS);
            player.openMenu(new SimpleMenuProvider((id, inventory, p)-> createMenu(id, inventory, p, item), item.getHoverName()));
        }
    }

    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player, ItemStack item) {
        return new StampKitMenu(containerId, inventory, getInventory(item));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
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

    private static final List<Item> ALL_SHERDS = List.of(
            ANGLER_POTTERY_SHERD,
            ARCHER_POTTERY_SHERD,
            ARMS_UP_POTTERY_SHERD,
            BLADE_POTTERY_SHERD,
            BREWER_POTTERY_SHERD,
            BURN_POTTERY_SHERD,
            DANGER_POTTERY_SHERD,
            EXPLORER_POTTERY_SHERD,
            FLOW_POTTERY_SHERD,
            FRIEND_POTTERY_SHERD,
            GUSTER_POTTERY_SHERD,
            HEART_POTTERY_SHERD,
            HEARTBREAK_POTTERY_SHERD,
            HOWL_POTTERY_SHERD,
            MINER_POTTERY_SHERD,
            MOURNER_POTTERY_SHERD,
            PLENTY_POTTERY_SHERD,
            PRIZE_POTTERY_SHERD,
            SCRAPE_POTTERY_SHERD,
            SHEAF_POTTERY_SHERD,
            SHELTER_POTTERY_SHERD,
            SKULL_POTTERY_SHERD,
            SNORT_POTTERY_SHERD
    );

}