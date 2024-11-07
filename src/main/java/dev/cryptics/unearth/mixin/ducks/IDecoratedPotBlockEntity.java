package dev.cryptics.unearth.mixin.ducks;

import dev.cryptics.unearth.common.blocks.entity.data.DecoratedPotColorLuminousData;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;

import java.util.Map;

public interface IDecoratedPotBlockEntity {
    DecoratedPotColorLuminousData getColorLuminousData();
    void setColor(Direction direction, int color);
    void setLuminous(Direction direction, boolean luminous);
    void markUpdated();
}
