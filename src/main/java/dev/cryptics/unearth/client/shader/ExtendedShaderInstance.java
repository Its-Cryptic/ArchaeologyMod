package dev.cryptics.unearth.client.shader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ChainedJsonException;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.GsonHelper;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class ExtendedShaderInstance extends ShaderInstance {

    public ExtendedShaderInstance(ResourceProvider pResourceProvider, ResourceLocation location, VertexFormat pVertexFormat) throws IOException {
        super(pResourceProvider, location, pVertexFormat);
    }

    public abstract ShaderHolder getShaderHolder();
}