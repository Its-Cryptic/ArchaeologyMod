package dev.cryptics.unearth.registry.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.cryptics.unearth.client.render.rendertype.RenderTypeProvider;
import net.minecraft.client.renderer.RenderType;

import static com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS;

public class UnearthRenderTypes extends RenderType {

    public static final RenderTypeProvider STAMP = new RenderTypeProvider((token) ->
            create("stamp", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, QUADS, 256, false, true,
                    CompositeState.builder()
                            .setShaderState(UnearthShaderRegistry.STAMP.getShard())
                            .setTextureState(token.get())
                            .setLightmapState(LIGHTMAP)
                            .createCompositeState(true)
            )
    );

    public UnearthRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }
}
