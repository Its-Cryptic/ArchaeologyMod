package dev.cryptics.unearth.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.cryptics.unearth.Unearth;
import dev.cryptics.unearth.common.blocks.StampBlock;
import dev.cryptics.unearth.common.blocks.entity.StampBlockEntity;
import dev.cryptics.unearth.registry.client.UnearthRenderTypes;
import dev.cryptics.unearth.registry.client.UnearthShaderRegistry;
import net.minecraft.Util;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class StampBlockEntityRenderer implements BlockEntityRenderer<StampBlockEntity> {

    public StampBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    private static final Map<Direction, Consumer<PoseStack>> directionConsumerMap = Util.make(new HashMap<>(), map -> {
        map.put(Direction.NORTH, poseStack -> {});
        map.put(Direction.SOUTH, poseStack -> {
            poseStack.translate(1, 0, 1);
            poseStack.mulPose(Axis.YP.rotationDegrees(180));
        });
        map.put(Direction.EAST, poseStack -> {
            poseStack.translate(1, 0, 0);
            poseStack.mulPose(Axis.YP.rotationDegrees(-90));
        });
        map.put(Direction.WEST, poseStack -> {
            poseStack.translate(0, 0, 1);
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
        });
        map.put(Direction.UP, poseStack -> {
            poseStack.translate(0, 1, 0);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
        });
        map.put(Direction.DOWN, poseStack -> {
            poseStack.translate(0, 0, 1);
            poseStack.mulPose(Axis.XP.rotationDegrees(-90));
        });
    });

    @Override
    public void render(StampBlockEntity stampBlockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        int color = stampBlockEntity.getColor();
        boolean luminous = stampBlockEntity.isLuminous();
        Direction direction = stampBlockEntity.getBlockState().getValue(StampBlock.FACING);
        Item sherdItem = stampBlockEntity.getSherdItem();

        ResourceLocation texture = CustomDecoratedPotRenderer.getTexture(sherdItem);
        //Unearth.LOGGER.info(getTexture(Items.ANGLER_POTTERY_SHERD).toString());
        renderStamp(direction, color, texture, poseStack, multiBufferSource, luminous ? LightTexture.FULL_BRIGHT : packedLight);
    }



    private static void renderStamp(Direction direction, int pColor, ResourceLocation pTexture, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        Color color = Color.of(pColor);
        RenderTypeToken token = RenderTypeToken.createCachedToken(pTexture);
        RenderType renderType = UnearthRenderTypes.STAMP.applyAndCache(token);
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);
        float offset = 1f/128f;
        poseStack.pushPose();
        directionConsumerMap.get(direction).accept(poseStack);
        poseStack.translate(0.0f, 0.0f, offset);

        posColorTexLightMap(vertexConsumer, poseStack, 0, 0, 0, 0.0f, 1.0f, color, packedLight);
        posColorTexLightMap(vertexConsumer, poseStack, 1, 0, 0, 1.0f, 1.0f, color, packedLight);
        posColorTexLightMap(vertexConsumer, poseStack, 1, 1, 0, 1.0f, 0.0f, color, packedLight);
        posColorTexLightMap(vertexConsumer, poseStack, 0, 1, 0, 0.0f, 0.0f, color, packedLight);

        poseStack.popPose();
    }

    private static void posColorTexLightMap(VertexConsumer vertexConsumer, PoseStack poseStack, int x, int y, int z, float u, float v, Color color, int packedLight) {
        vertexConsumer.addVertex(poseStack.last(), x, y, z).setColor(color.r(), color.g(), color.b(), color.a()).setUv(u, v).setLight(packedLight);
    }

    private ResourceLocation getTexture(Item item) {
        ResourceLocation itemRl = BuiltInRegistries.ITEM.getKey(item);
        String itemPath = itemRl.getPath();
        itemPath = itemPath.substring(0, itemPath.length() - 14);
        return Unearth.id("textures/stamps/" + itemPath + ".png");
    }

}
