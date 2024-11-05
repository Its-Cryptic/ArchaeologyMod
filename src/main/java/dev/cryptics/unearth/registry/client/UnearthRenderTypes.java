package dev.cryptics.unearth.registry.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.cryptics.unearth.Unearth;
import dev.cryptics.unearth.client.render.rendertype.RenderTypeProvider;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP;
import static com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS;

public class UnearthRenderTypes extends RenderType {


//    public static final RenderType STAMP = create("my_block_entity", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 2097152, false, true,
//            CompositeState.builder()
//                    .setShaderState(STAMP_SHADER)
//                    .createCompositeState(true)
//    );

    public static final RenderTypeProvider STAMP = new RenderTypeProvider((token) ->
            create("stamp", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, QUADS, 256, false, true,
                    CompositeState.builder()
                            .setShaderState(UnearthShaderRegistry.STAMP.getShard())
                            .setTextureState(token.get())
                            .setLightmapState(LIGHTMAP)
                            .createCompositeState(true)
            )
    );

    public static final RenderType STAMP2 = create("stamp2", POSITION_COLOR_TEX_LIGHTMAP, QUADS, 256, false, true,
            CompositeState.builder()
                    //.setShaderState(new ShaderStateShard(GameRenderer::getPositionColorTexLightmapShader))
                    .setShaderState(new ShaderStateShard(UnearthShaderRegistry.STAMP.getInstance()))
                    .setTextureState(new RenderStateShard.TextureStateShard(Unearth.id("textures/stamps/angler.png"), false, false))
                    .setLightmapState(LIGHTMAP)
                    .createCompositeState(true)
    );

    public UnearthRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }
}
