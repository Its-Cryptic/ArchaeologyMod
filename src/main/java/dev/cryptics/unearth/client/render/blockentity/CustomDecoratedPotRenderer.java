package dev.cryptics.unearth.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.cryptics.unearth.Unearth;
import dev.cryptics.unearth.client.render.rendertype.RenderTypeToken;
import dev.cryptics.unearth.common.blocks.entity.data.DecoratedPotColorLuminousData;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class CustomDecoratedPotRenderer {
    public static void renderStamp(Direction direction, IDecoratedPotBlockEntity blockEntity, Optional<Item> pItem, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        if (pItem.isEmpty()) return;
        if (pItem.get().equals(Items.BRICK)) return;

        DecoratedPotColorLuminousData.Entry entry = blockEntity.getColorLuminousData().get(direction.getOpposite());
        if (!entry.isColored()) return;
        //int color = blockEntity.getColorsMap().getOrDefault(direction, -1);
        int color = entry.getColor();
        //Unearth.LOGGER.info("Color: " + color);
        //Unearth.LOGGER.info("R: " + (color >> 16 & 255) +  ", G: " + (color >> 8 & 255) + ", B: " + (color & 255));
        //packedLight = blockEntity.getLuminousMap().getOrDefault(direction, false) ? LightTexture.FULL_BRIGHT : packedLight;
        packedLight = entry.isLuminous() ? LightTexture.FULL_BRIGHT : packedLight;
        renderStamp(direction, color, pItem.get(), poseStack, multiBufferSource, packedLight);
    }

    public static void renderStamp(Direction direction, int packedColor, Item item, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        Color color = Color.of(packedColor);
        ResourceLocation pTexture = getTexture(item);
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

    public static ResourceLocation getTexture(Item item) {
        ResourceLocation itemRl = BuiltInRegistries.ITEM.getKey(item);
        String itemPath = itemRl.getPath();
        itemPath = itemPath.substring(0, itemPath.length() - 14);
        return Unearth.id("textures/stamps/" + itemPath + ".png");
    }
}
