package dev.cryptics.unearth.common.blocks.entity.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.PotDecorations;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public record PotColors(Optional<DyeColor> back, Optional<DyeColor> left, Optional<DyeColor> right, Optional<DyeColor> front) {
    public static final PotColors EMPTY = new PotColors(Optional.of(DyeColor.BLUE), Optional.of(DyeColor.BROWN), Optional.empty(), Optional.empty());

    public static final Codec<PotColors> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DyeColor.CODEC.optionalFieldOf("back").forGetter(PotColors::back),
            DyeColor.CODEC.optionalFieldOf("left").forGetter(PotColors::left),
            DyeColor.CODEC.optionalFieldOf("right").forGetter(PotColors::right),
            DyeColor.CODEC.optionalFieldOf("front").forGetter(PotColors::front)
    ).apply(instance, PotColors::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PotColors> STREAM_CODEC = StreamCodec.composite(
            optionalStreamCodec(DyeColor.STREAM_CODEC), PotColors::back,
            optionalStreamCodec(DyeColor.STREAM_CODEC), PotColors::left,
            optionalStreamCodec(DyeColor.STREAM_CODEC), PotColors::right,
            optionalStreamCodec(DyeColor.STREAM_CODEC), PotColors::front,
            PotColors::new
    );

    public static <T> StreamCodec<ByteBuf, Optional<T>> optionalStreamCodec(StreamCodec<ByteBuf, T> codec) {
        return StreamCodec.of(
                (buf, optionalValue) -> {
                    if (optionalValue.isPresent()) {
                        buf.writeBoolean(true); // Presence flag
                        codec.encode(buf, optionalValue.get()); // Encode the actual value
                    } else {
                        buf.writeBoolean(false); // Absence flag
                    }
                },
                buf -> {
                    boolean isPresent = buf.readBoolean(); // Read the presence flag
                    if (isPresent) {
                        return Optional.of(codec.decode(buf)); // Decode the actual value
                    } else {
                        return Optional.empty(); // Return empty if not present
                    }
                }
        );
    }

    public CompoundTag save(CompoundTag tag) {
        if (!this.equals(EMPTY)) {
            tag.put("colors", CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow());
        }
        return tag;
    }

    public static PotColors load(@Nullable CompoundTag tag) {
        return tag != null && tag.contains("colors") ? CODEC.parse(NbtOps.INSTANCE, tag.get("colors")).result().orElse(EMPTY) : EMPTY;
    }
}
