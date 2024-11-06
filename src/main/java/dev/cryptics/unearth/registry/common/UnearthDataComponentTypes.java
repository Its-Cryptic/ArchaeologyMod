package dev.cryptics.unearth.registry.common;

import dev.cryptics.unearth.common.blocks.entity.data.PotColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static dev.cryptics.unearth.Unearth.MODID;

public class UnearthDataComponentTypes {
    public static DeferredRegister<DataComponentType<?>> DATA = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, MODID);
    public static final Supplier<DataComponentType<BlockPos>> COORDINATES = register("coordinates", builder ->
            builder.persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC)
    );

    public static final Supplier<DataComponentType<PotColors>> POT_COLORS = register("pot_colors", builder ->
            builder.persistent(PotColors.CODEC).networkSynchronized(PotColors.STREAM_CODEC)
    );

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void init(IEventBus modEventBus) {
        DATA.register(modEventBus);
    }
}
