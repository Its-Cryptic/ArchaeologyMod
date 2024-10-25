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
        int textColor = dyeColor.getTextColor();
        int r = (textColor >> 16) & 0xFF;
        int g = (textColor >> 8) & 0xFF;
        int b = textColor & 0xFF;
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
