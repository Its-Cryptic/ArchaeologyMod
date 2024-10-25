package dev.cryptics.unearth.registry.client;

import team.lodestar.lodestone.registry.client.LodestoneRenderTypes;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeProvider;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP;
import static com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS;

public class UnearthRenderTypes extends LodestoneRenderTypes {

    public UnearthRenderTypes(String p_110161_, Runnable p_110162_, Runnable p_110163_) {
        super(p_110161_, p_110162_, p_110163_);
    }

//    public static final RenderType STAMP = create("my_block_entity", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 2097152, false, true,
//            CompositeState.builder()
//                    .setShaderState(STAMP_SHADER)
//                    .createCompositeState(true)
//    );

    public static final RenderTypeProvider STAMP = new RenderTypeProvider((token) ->
            createGenericRenderType("stamp", POSITION_COLOR_TEX_LIGHTMAP, QUADS, builder()
                    .setShaderState(UnearthShaderRegistry.STAMP)
                    //.setTransparencyState(StateShards.TRANSLUCENT_TRANSPARENCY)
                    //.setLightmapState(LIGHTMAP)
                    //.setCullState(CULL)
                    .setTextureState(token.get())));

}
