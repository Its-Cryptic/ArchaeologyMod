package dev.cryptics.unearth.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.cryptics.unearth.Unearth;
import dev.cryptics.unearth.mixin.ducks.IDecoratedPotBlockEntity;
import dev.cryptics.unearth.registry.client.UnearthRenderTypes;
import net.minecraft.Util;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class CustomDecoratedPotRenderer {

    public static void renderStamp(Direction direction, IDecoratedPotBlockEntity blockEntity, Optional<Item> item, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        if (item.isEmpty()) return;
        if (item.get().equals(Items.BRICK)) return;
        DyeColor color = blockEntity.getColorsMap().get(direction);
        packedLight = blockEntity.getLuminousMap().getOrDefault(direction, false) ? LightTexture.FULL_BRIGHT : packedLight;
        if (color == null) return;
        renderStamp(direction, color, item, poseStack, multiBufferSource, packedLight);
    }

    public static void renderStamp(Direction direction, DyeColor pColor, Optional<Item> item, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        Color color = Color.of(pColor);
        ResourceLocation pTexture = item.map(CustomDecoratedPotRenderer::getTexture).orElse(Unearth.id("textures/gui/container/backpack.png"));
        RenderTypeToken token = RenderTypeToken.createCachedToken(pTexture);
        RenderType renderType = UnearthRenderTypes.STAMP.applyAndCache(token);
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);

        float offset = 1f/256f-1f/16f;
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 1.0f);
        directionConsumerMap.get(direction).accept(poseStack);
        poseStack.translate(0.0f, 0.0f, offset);
        posColorTexLightMap(vertexConsumer, poseStack, 0, 0, 0, 0.0f, 1.0f, color, packedLight);
        posColorTexLightMap(vertexConsumer, poseStack, 1, 0, 0, 1.0f, 1.0f, color, packedLight);
        posColorTexLightMap(vertexConsumer, poseStack, 1, 1, 0, 1.0f, 0.0f, color, packedLight);
        posColorTexLightMap(vertexConsumer, poseStack, 0, 1, 0, 0.0f, 0.0f, color, packedLight);

        poseStack.popPose();
    }

    public static Map<Direction, Consumer<PoseStack>> directionConsumerMap = Util.make(new HashMap<>(), map -> {
        map.put(Direction.NORTH, poseStack -> {});
        map.put(Direction.SOUTH, poseStack -> {
            poseStack.translate(1, 0, -1);
            poseStack.mulPose(Axis.YP.rotationDegrees(180));
        });
        map.put(Direction.EAST, poseStack -> {
            poseStack.translate(0, 0, -1);
            poseStack.mulPose(Axis.YP.rotationDegrees(-90));
        });
        map.put(Direction.WEST, poseStack -> {
            poseStack.translate(1, 0, 0);
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
        });
    });

    private static void posColorTexLightMap(VertexConsumer vertexConsumer, PoseStack poseStack, int x, int y, int z, float u, float v, Color color, int packedLight) {
        vertexConsumer.addVertex(poseStack.last(), x, y, z).setColor(color.r(), color.g(), color.b(), color.a()).setUv(u, v).setLight(packedLight);
    }

    private static ResourceLocation getTexture(Item item) {
        ResourceLocation itemRl = BuiltInRegistries.ITEM.getKey(item);
        String itemPath = itemRl.getPath();
        itemPath = itemPath.substring(0, itemPath.length() - 14);
        return Unearth.id("textures/stamps/" + itemPath + ".png");
    }
}
