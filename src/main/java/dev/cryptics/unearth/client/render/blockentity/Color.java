package dev.cryptics.unearth.client.render.blockentity;

import net.minecraft.world.item.DyeColor;

public class Color {
    public int r,g,b,a;
    public Color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public static Color of(DyeColor dyeColor) {
        return of(dyeColor.getTextColor());
    }

    public static Color of(int packedColor) {
        int r = (packedColor >> 16) & 0xFF;
        int g = (packedColor >> 8) & 0xFF;
        int b = packedColor & 0xFF;
        return new Color(r, g, b, 255);
    }

    public int r() {
        return r;
    }

    public int g() {
        return g;
    }

    public int b() {
        return b;
    }

    public int a() {
        return a;
    }
}
