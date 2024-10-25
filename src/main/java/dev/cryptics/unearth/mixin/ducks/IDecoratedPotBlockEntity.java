package dev.cryptics.unearth.mixin.ducks;

import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;

import java.util.Map;

public interface IDecoratedPotBlockEntity {
    Map<Direction, DyeColor> getColorsMap();
    Map<Direction, Boolean> getLuminousMap();
    void setColor(Direction direction, DyeColor color);
    void setLuminous(Direction direction, boolean luminous);
    void markUpdated();
}
