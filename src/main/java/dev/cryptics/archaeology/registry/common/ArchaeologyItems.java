package dev.cryptics.archaeology.registry.common;

import dev.cryptics.archaeology.ArchaeologyMod;
import dev.cryptics.archaeology.common.items.Stamp;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ArchaeologyItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ArchaeologyMod.MODID);

    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    public static final DeferredItem<Item> EXAMPLE_ITEM2 = ITEMS.register("example_item2", () ->
            new Item(new Item.Properties()
                        .food(new FoodProperties.Builder()
                                .alwaysEdible()
                                .nutrition(1)
                                .saturationModifier(2f)
                                .build()
                        )
            )
    );

    public static final DeferredItem<Stamp> STAMP = ITEMS.register("stamp_kit", Stamp::new);

    public static void init(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
