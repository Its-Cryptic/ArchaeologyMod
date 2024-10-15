package dev.cryptics.archaeology.registry.common;

import dev.cryptics.archaeology.ArchaeologyMod;
import dev.cryptics.archaeology.common.blocks.StampBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ArchaeologyBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ArchaeologyMod.MODID);

    public static final DeferredBlock<StampBlock> STAMP_BLOCK = BLOCKS.register("angler_stamp", () ->
            new StampBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .replaceable()
                    .noCollission()
                    .randomTicks()
                    .strength(0.2F)
                    .sound(SoundType.VINE)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
