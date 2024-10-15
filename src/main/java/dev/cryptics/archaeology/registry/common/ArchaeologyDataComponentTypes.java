package dev.cryptics.archaeology.registry.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static dev.cryptics.archaeology.ArchaeologyMod.MODID;

public class ArchaeologyDataComponentTypes {
    public static DeferredRegister<DataComponentType<?>> DATA = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, MODID);

    //public static final DeferredHolder<DataComponentType<?>, DataComponentType<SpellCaster>> SPELL_CASTER = DATA.register("stamp", () -> DataComponentType.<SpellCaster>builder().persistent(SpellCaster.CODEC.codec()).networkSynchronized(SpellCaster.STREAM_CODEC).build());
    public static final Supplier<DataComponentType<BlockPos>> COORDINATES = register("coordinates", builder ->
            builder.persistent(BlockPos.CODEC)
    );

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void init(IEventBus modEventBus) {
        DATA.register(modEventBus);
    }
}
