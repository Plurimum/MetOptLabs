package com.mygdx.newton;

import com.mygdx.nmethods.AbstractNMethod;
import com.mygdx.nmethods.Value;
import com.mygdx.nmethods.Vector;

import java.util.List;

public class ClassicNewtonMethod<F extends NewtonFunction> extends AbstractNMethod<F> {

    public ClassicNewtonMethod(final F f) {
        super(f);
    }

    public ClassicNewtonMethod(final F f, final Vector start) {
        super(f, start);
    }

    @Override
    public Value<Vector, Double> nextIteration(final Value<Vector, Double> x, final double eps) {
        final Vector gradient = getFunction().gradient(x.getValue());
        final Vector p = new Vector(getFunction().hesse(x.getValue()).solve(gradient.multiply(-1)));
        return p.length() < eps ? null : new Value<>(x.getValue().add(p), getFunction());
    }

}
