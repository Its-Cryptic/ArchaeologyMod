package dev.cryptics.archaeology.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.cryptics.archaeology.ArchaeologyMod;
import dev.cryptics.archaeology.common.blocks.StampBlock;
import dev.cryptics.archaeology.common.blocks.entity.StampBlockEntity;
import dev.cryptics.archaeology.registry.client.ArchaeologyRenderTypes;
import net.minecraft.Util;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
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
        DyeColor color = stampBlockEntity.getColor();
        Direction direction = stampBlockEntity.getBlockState().getValue(StampBlock.FACING);

        ResourceLocation texture = ArchaeologyMod.id("textures/stamps/heart.png");
        renderStamp(direction, color, texture, poseStack, multiBufferSource);
    }



    private static void renderStamp(Direction direction, DyeColor pColor, ResourceLocation pTexture, PoseStack poseStack, MultiBufferSource multiBufferSource) {
        Color color = Color.of(pColor);
        RenderTypeToken token = RenderTypeToken.createCachedToken(pTexture);
        RenderType renderType = ArchaeologyRenderTypes.STAMP.applyAndCache(token);
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);

        float offset = 1f/64f;
        poseStack.pushPose();
        directionConsumerMap.get(direction).accept(poseStack);
        poseStack.translate(0.0f, 0.0f, offset);

        posColorTexLightMap(vertexConsumer, poseStack, 0, 0, 0, 0.0f, 1.0f, color, LightTexture.FULL_BRIGHT);
        posColorTexLightMap(vertexConsumer, poseStack, 1, 0, 0, 1.0f, 1.0f, color, LightTexture.FULL_BRIGHT);
        posColorTexLightMap(vertexConsumer, poseStack, 1, 1, 0, 1.0f, 0.0f, color, LightTexture.FULL_BRIGHT);
        posColorTexLightMap(vertexConsumer, poseStack, 0, 1, 0, 0.0f, 0.0f, color, LightTexture.FULL_BRIGHT);

        poseStack.popPose();
    }

    private static void posColorTexLightMap(VertexConsumer vertexConsumer, PoseStack poseStack, int x, int y, int z, float u, float v, Color color, int packedLight) {
        vertexConsumer.addVertex(poseStack.last(), x, y, z).setColor(color.r(), color.g(), color.b(), color.a()).setUv(u, v).setLight(packedLight);
    }

    private static class Color {
        public int r,g,b,a;
        public Color(int r, int g, int b, int a) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }

        public static Color of(DyeColor dyeColor) {
            int textColor = dyeColor.getTextColor();
            int r = (textColor >> 16) & 0xFF;
            int g = (textColor >> 8) & 0xFF;
            int b = textColor & 0xFF;
            return new Color(r, g, b, 255);
        }

        public int r() {
            return r;
        }

        public int g() {
            return g;
        }

        public int b() {
            return b;
        }

        public int a() {
            return a;
        }
    }


}
