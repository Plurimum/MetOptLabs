package com.mygdx.nmethods;

public abstract class AbstractNMethod<F extends NFunction> extends AbstractFunctionNMethod<F> {

    protected AbstractNMethod(final F function) {
        super(function);
    }

    protected AbstractNMethod(final F function, final Vector start) {
        super(function, start);
    }

    public abstract Value<Vector, Double> nextIteration(final Value<Vector, Double> x, final double eps);

    @Override
    public Vector findMin(double eps) {
        Value<Vector, Double> x = new Value<>(getStart(), getFunction());
        Value<Vector, Double> y;

        while ((y = nextIteration(x, eps)) != null) {
            x = y;
        }
        return x.getValue();
    }
}
