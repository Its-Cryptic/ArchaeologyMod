package dev.cryptics.unearth.mixin.mixins.common;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.cryptics.unearth.Unearth;
import dev.cryptics.unearth.common.blocks.entity.data.DecoratedPotColorLuminousData;
import dev.cryptics.unearth.registry.common.UnearthDataComponentTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Items.class)
public class ItemsMixin {

//    @ModifyExpressionValue(
//            method = "<clinit>",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/world/item/Item$Properties;component(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Lnet/minecraft/world/item/Item$Properties;",
//                    ordinal = 0
//            ),
//            slice = @Slice(
//                    from = @At(
//                            value = "FIELD", target = "Lnet/minecraft/world/item/Items;DECORATED_POT:Lnet/minecraft/world/item/Item;",
//                            ordinal = 0
//                    )
//            )
//    )
//    private static Item.Properties unearth$addComponentTo(Item.Properties original) {
//        //return original.component(UnearthDataComponentTypes.POT_COLORS.get(), DecoratedPotColorLuminousData.EMPTY);
//        return original.component(DataComponents.BASE_COLOR, DyeColor.BLUE);
//    }
}
