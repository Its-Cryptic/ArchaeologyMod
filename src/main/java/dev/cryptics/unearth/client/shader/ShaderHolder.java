package dev.cryptics.unearth.client.shader;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class ShaderHolder {

    public final ResourceLocation shaderLocation;
    public final VertexFormat shaderFormat;

    protected ExtendedShaderInstance shaderInstance;
    private final RenderStateShard.ShaderStateShard shard = new RenderStateShard.ShaderStateShard(getInstance());

    public ShaderHolder(ResourceLocation shaderLocation, VertexFormat shaderFormat) {
        this.shaderLocation = shaderLocation;
        this.shaderFormat = shaderFormat;
    }

    public ExtendedShaderInstance createInstance(ResourceProvider provider) throws IOException {
        ShaderHolder shaderHolder = this;
        ExtendedShaderInstance shaderInstance = new ExtendedShaderInstance(provider, shaderLocation, shaderFormat) {
            @Override
            public ShaderHolder getShaderHolder() {
                return shaderHolder;
            }
        };
        this.shaderInstance = shaderInstance;
        return shaderInstance;
    }

    public Supplier<ShaderInstance> getInstance() {
        return () -> shaderInstance;
    }

    public void setShaderInstance(ShaderInstance reloadedShaderInstance) {
        this.shaderInstance = (ExtendedShaderInstance) reloadedShaderInstance;
    }

    public RenderStateShard.ShaderStateShard getShard() {
        return shard;
    }
}