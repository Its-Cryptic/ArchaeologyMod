package dev.cryptics.unearth.registry.common;

import dev.cryptics.unearth.Unearth;
import dev.cryptics.unearth.common.items.StampItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;

public class UnearthItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Unearth.MODID);

    public static final DeferredItem<StampItem> STAMP = ITEMS.register("stamp_kit", StampItem::new);

    public static final DeferredItem<Item> CONVERSATION_RANGE_MUSIC_DISC = ITEMS.register("conversation_range_music_disc", () ->
            new Item(new Item.Properties()
                    .jukeboxPlayable(UnearthSounds.CONVERSATION_RANGE_KEY)
                    .stacksTo(1)
                    .rarity(Rarity.RARE)
            )
    );

    public static void forEachItem(Consumer<DeferredHolder<Item, ? extends Item>> consumer) {
        ITEMS.getEntries().forEach(consumer);
    }

    public static void init(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
