package dev.cryptics.unearth.common.blocks.entity.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import javax.annotation.Nullable;

public class DecoratedPotColorLuminousData {
    private Entry north, east, south, west;

    public static final DecoratedPotColorLuminousData EMPTY = new DecoratedPotColorLuminousData(Entry.EMPTY);

    public static final Codec<DecoratedPotColorLuminousData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Entry.CODEC.fieldOf("north").forGetter(data -> data.get(Direction.NORTH)),
            Entry.CODEC.fieldOf("east").forGetter(data -> data.get(Direction.EAST)),
            Entry.CODEC.fieldOf("south").forGetter(data -> data.get(Direction.SOUTH)),
            Entry.CODEC.fieldOf("west").forGetter(data -> data.get(Direction.WEST))
    ).apply(instance, DecoratedPotColorLuminousData::new));

    public static final StreamCodec<ByteBuf, DecoratedPotColorLuminousData> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public DecoratedPotColorLuminousData(Entry north, Entry east, Entry south, Entry west) {
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
    }

    public DecoratedPotColorLuminousData(Entry packedData) {
        this(packedData.copy(), packedData.copy(), packedData.copy(), packedData.copy());
    }

    public DecoratedPotColorLuminousData(int color, boolean luminous) {
        this(new Entry((luminous ? 1 : 0) << 25 | 1 << 24 | color));
    }

    public DecoratedPotColorLuminousData(int r, int g, int b, boolean luminous) {
        this(new Entry((luminous ? 1 : 0) << 25 | 1 << 24 | r << 16 | g << 8 | b));
    }

    public Entry get(Direction direction) {
        return switch (direction) {
            case NORTH -> this.north;
            case EAST -> this.east;
            case SOUTH -> this.south;
            case WEST -> this.west;
            default -> null;
        };
    }

    public CompoundTag save(CompoundTag tag) {
//        if (!this.equals(EMPTY)) {
//            tag.put("colors_test", CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow());
//        }
        tag.put("unearth", CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow());
        return tag;
    }

    public static DecoratedPotColorLuminousData load(@Nullable CompoundTag tag) {
        return tag != null && tag.contains("unearth") ? CODEC.parse(NbtOps.INSTANCE, tag.get("unearth")).result().orElse(EMPTY) : EMPTY;
    }

    public DecoratedPotColorLuminousData copy() {
        return new DecoratedPotColorLuminousData(this.north.copy(), this.east.copy(), this.south.copy(), this.west.copy());
    }

    public static class Entry {
        // Extra    R        G        B
        // 00000000 00000000 00000000 00000000
        // Bits 31-26: Unused
        // Bit 25: Luminous
        // Bit 24: IsColored
        // Bits 23-16: Red (0-255)
        // Bits 15-8: Green (0-255)
        // Bits 7-0: Blue (0-255)

        // 0 << 25 | 0 << 24 | 0 << 16 | 0 << 8 | 0;
        private int packedData;
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("packedData").forGetter(Entry::getPackedData)
        ).apply(instance, Entry::new));
        public static final StreamCodec<ByteBuf, Entry> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
        public static Entry EMPTY = new Entry(0);

        public Entry(int packedData) {
            this.packedData = packedData;
        }

        public int getPackedData() {
            return this.packedData;
        }

        public void setPackedData(int packedData) {
            this.packedData = packedData;
        }

        public int getColor() {
            return this.packedData & 0xFFFFFF;
        }

        public void setColor(int color) {
            this.packedData = this.packedData & 0xFF000000 | color;
            this.setColored(true);
        }

        public boolean isLuminous() {
            return (this.packedData & 0x2000000) != 0;
        }

        public void setLuminous(boolean luminous) {
            this.packedData = this.packedData & 0xFDFFFFFF | (luminous ? 1 : 0) << 25;
        }

        public boolean isColored() {
            return (this.packedData & 0x01000000) != 0;
        }

        public void setColored(boolean useColor) {
            this.packedData = this.packedData & 0xFEFFFFFF | (useColor ? 1 : 0) << 24;
        }

        public Entry copy() {
            return new Entry(this.packedData);
        }
    }
}
