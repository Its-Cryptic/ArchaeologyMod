package dev.cryptics.unearth.registry.common;

import dev.cryptics.unearth.Unearth;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.stream.Collectors;

public class UnearthCreativeModeTabs {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Unearth.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> UNEARTH_TAB = CREATIVE_MODE_TABS.register("unearth", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.unearth"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> UnearthItems.STAMP.get().getDefaultInstance())
            .displayItems((parameters, output) -> 
                    output.acceptAll(UnearthItems.ITEMS
                            .getEntries()
                            .stream()
                            .map(DeferredHolder::get)
                            .map(Item::getDefaultInstance)
                            .collect(Collectors.toList())
                    )
            )
            .build()
    );


    public static void init(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
