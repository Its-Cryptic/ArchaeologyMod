package dev.cryptics.unearth.registry.common;

import dev.cryptics.unearth.common.blocks.entity.StampBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion.MOD_ID;

public class UnearthBlockEntitites {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MOD_ID);

    public static final Supplier<BlockEntityType<StampBlockEntity>> STAMP_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("stamp_block_entity",
            () -> BlockEntityType.Builder.of(StampBlockEntity::new, UnearthBlocks.STAMP_BLOCK.get())
                    .build(null)
    );

    public static void init(IEventBus modEventBus) {
        BLOCK_ENTITY_TYPES.register(modEventBus);
    }
}
