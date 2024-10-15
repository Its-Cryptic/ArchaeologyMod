package dev.cryptics.archaeology.registry.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import dev.cryptics.archaeology.ArchaeologyMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import team.lodestar.lodestone.registry.client.LodestoneShaders;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

@EventBusSubscriber(value = Dist.CLIENT, modid = ArchaeologyMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ArchaeologyShaderRegistry {

    public static ShaderHolder STAMP = new ShaderHolder(ArchaeologyMod.id("stamp"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) {
        LodestoneShaders.registerShader(event, STAMP);
    }



}
