package dev.cryptics.unearth.registry.common;

import dev.cryptics.unearth.Unearth;
import dev.cryptics.unearth.client.screen.StampKitMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class UnearthMenuTypes {
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(BuiltInRegistries.MENU, Unearth.MODID);

    public static final Supplier<MenuType<StampKitMenu>> STAMP_KIT_MENU = CONTAINERS.register("stamp_kit", () -> new MenuType<>(StampKitMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static void init(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}
