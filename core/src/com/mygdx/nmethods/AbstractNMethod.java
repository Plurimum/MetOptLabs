package com.mygdx.nmethods;

import java.util.Collections;

public abstract class AbstractNMethod<F extends NFunction> implements NMethod {
    private final F function;
    private final Vector start;

    protected AbstractNMethod(final F function) {
        this.function = function;
        this.start = new Vector(Collections.nCopies(function.getN(), 0.));
    }

    protected AbstractNMethod(final F function, final Vector start) {
        this.function = function;
        this.start = start;
    }

    public F getFunction() {
        return function;
    }

    public abstract Value<Vector, Double> nextIteration(final Value<Vector, Double> x, final double eps);

    @Override
    public Vector findMin(double eps) {
        Value<Vector, Double> x = new Value<>(start, function);
        Value<Vector, Double> y;

        while ((y = nextIteration(x, eps)) != null) {
            x = y;
        }
        return x.getValue();
    }
}
