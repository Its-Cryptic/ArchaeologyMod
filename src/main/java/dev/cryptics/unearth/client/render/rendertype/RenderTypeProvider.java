package dev.cryptics.unearth.client.render.rendertype;

import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;

import java.util.function.Function;

public class RenderTypeProvider {
    private final Function<RenderTypeToken, RenderType> function;
    private final Function<RenderTypeToken, RenderType> memorizedFunction;

    public RenderTypeProvider(Function<RenderTypeToken, RenderType> function) {
        this.function = function;
        this.memorizedFunction = Util.memoize(function);
    }

    public RenderType apply(RenderTypeToken token) {
        return function.apply(token);
    }

    public RenderType applyAndCache(RenderTypeToken token) {
        return this.memorizedFunction.apply(token);
    }
}