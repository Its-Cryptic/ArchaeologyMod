package dev.cryptics.unearth.registry.common;

import dev.cryptics.unearth.Unearth;
import dev.cryptics.unearth.common.items.StampItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class UnearthItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Unearth.MODID);

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

    public static final DeferredItem<StampItem> STAMP = ITEMS.register("stamp_kit", StampItem::new);

    public static void init(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
