package dev.cryptics.unearth;

import dev.cryptics.unearth.client.render.blockentity.StampBlockEntityRenderer;
import dev.cryptics.unearth.client.screen.StampKitScreen;
import dev.cryptics.unearth.compat.PastelCompat;
import dev.cryptics.unearth.config.*;
import dev.cryptics.unearth.registry.common.*;
import dev.cryptics.unearth.registry.common.UnearthBlockEntitites;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Unearth.MODID)
public class Unearth {
    public static final String MODID = "unearth";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Unearth(IEventBus modEventBus, ModContainer modContainer) {
        UnearthItems.init(modEventBus);
        UnearthBlocks.init(modEventBus);
        UnearthBlockEntitites.init(modEventBus);
        UnearthCreativeModeTabs.init(modEventBus);
        UnearthSounds.init(modEventBus);
        UnearthMenuTypes.init(modEventBus);

        UnearthDataComponentTypes.init(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);

        PastelCompat.init();
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }

        @SubscribeEvent
        public static void menuScreens(RegisterMenuScreensEvent event){
            event.register(UnearthMenuTypes.STAMP_KIT_MENU.get(), StampKitScreen::new);
        }

        @SubscribeEvent
        public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(UnearthBlockEntitites.STAMP_BLOCK_ENTITY.get(), StampBlockEntityRenderer::new);
        }
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
