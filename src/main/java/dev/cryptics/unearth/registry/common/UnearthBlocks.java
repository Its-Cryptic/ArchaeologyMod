package dev.cryptics.unearth.registry.common;

import dev.cryptics.unearth.Unearth;
import dev.cryptics.unearth.common.blocks.StampBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class UnearthBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Unearth.MODID);

    public static final DeferredBlock<StampBlock> STAMP_BLOCK = BLOCKS.register("angler_stamp", () ->
            new StampBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.CLAY)
                    .noCollission()
                    .strength(0.2F)
                    .sound(SoundType.SAND)
                    .pushReaction(PushReaction.DESTROY)
                    .noLootTable()
            )
    );

    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
