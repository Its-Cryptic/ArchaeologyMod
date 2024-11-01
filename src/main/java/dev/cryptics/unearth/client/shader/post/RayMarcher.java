package dev.cryptics.unearth.client.shader.post;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cryptics.unearth.Unearth;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.systems.postprocess.PostProcessor;

import java.util.Arrays;

public class RayMarcher extends PostProcessor {
    @Override
    public ResourceLocation getPostChainLocation() {
        return Unearth.id("ray_marcher");
    }

    @Override
    public void beforeProcess(PoseStack viewModelStack) {
        for (EffectInstance effect : this.effects) {
            effect.safeGetUniform("u_time").set(Minecraft.getInstance().level.getGameTime()/20f);
        }
    }

    @Override
    public void afterProcess() {

    }
}