package dev.cryptics.unearth.registry.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import dev.cryptics.unearth.Unearth;
import dev.cryptics.unearth.client.shader.ShaderHolder;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;

@EventBusSubscriber(value = Dist.CLIENT, modid = Unearth.MODID, bus = EventBusSubscriber.Bus.MOD)
public class UnearthShaderRegistry {

    public static ShaderHolder STAMP = new ShaderHolder(Unearth.id("stamp"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) {
        registerShader(event, STAMP);
    }

    public static void registerShader(RegisterShadersEvent event, ShaderHolder shaderHolder) {
        try {
            ResourceProvider provider = event.getResourceProvider();
            event.registerShader(shaderHolder.createInstance(provider), shaderHolder::setShaderInstance);
        } catch (IOException e) {
            Unearth.LOGGER.error("Error registering shader", e);
            e.printStackTrace();
        }
    }



}
