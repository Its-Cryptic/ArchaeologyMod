package dev.cryptics.unearth.mixin.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.cryptics.unearth.Unearth;
import dev.cryptics.unearth.client.render.blockentity.CustomDecoratedPotRenderer;
import dev.cryptics.unearth.mixin.ducks.IDecoratedPotBlockEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import net.minecraft.world.level.block.entity.PotDecorations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(DecoratedPotRenderer.class)
public abstract class DecoratedPotRendererMixin implements BlockEntityRenderer<DecoratedPotBlockEntity> {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V"))
    public void renderColoredDecals(DecoratedPotBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, CallbackInfo ci) {
        PotDecorations potdecorations = blockEntity.getDecorations();
        IDecoratedPotBlockEntity decoratedPotBlockEntity = (IDecoratedPotBlockEntity) (Object) blockEntity;
        CustomDecoratedPotRenderer.renderStamp(Direction.NORTH, decoratedPotBlockEntity, potdecorations.front(), poseStack, bufferSource, packedLight);
        CustomDecoratedPotRenderer.renderStamp(Direction.SOUTH, decoratedPotBlockEntity, potdecorations.back(), poseStack, bufferSource, packedLight);
        CustomDecoratedPotRenderer.renderStamp(Direction.EAST, decoratedPotBlockEntity, potdecorations.left(), poseStack, bufferSource, packedLight);
        CustomDecoratedPotRenderer.renderStamp(Direction.WEST, decoratedPotBlockEntity, potdecorations.right(), poseStack, bufferSource, packedLight);
    }
}
