package dev.cryptics.archaeology.registry.common;

import dev.cryptics.archaeology.common.blocks.entity.StampBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion.MOD_ID;

public class ArchaeologyBlockEntitites {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MOD_ID);

    public static final Supplier<BlockEntityType<StampBlockEntity>> STAMP_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("my_block_entity",
            () -> BlockEntityType.Builder.of(StampBlockEntity::new, ArchaeologyBlocks.STAMP_BLOCK.get())
                    .build(null)
    );

    public static void init(IEventBus modEventBus) {
        BLOCK_ENTITY_TYPES.register(modEventBus);
    }
}
