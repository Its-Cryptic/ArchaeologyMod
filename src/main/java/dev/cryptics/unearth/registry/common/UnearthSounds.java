package dev.cryptics.unearth.registry.common;

import dev.cryptics.unearth.Unearth;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class UnearthSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Unearth.MODID);


    public static final Supplier<SoundEvent> CONVERSATION_RANGE = registerSoundEvent("conversation_range");
    public static final ResourceKey<JukeboxSong> CONVERSATION_RANGE_KEY = createSong("conversation_range");

    private static ResourceKey<JukeboxSong> createSong(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, Unearth.id(name));
    }

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(Unearth.id(name)));
    }

    public static void init(IEventBus modEventBus) {
        SOUND_EVENTS.register(modEventBus);
    }
}
