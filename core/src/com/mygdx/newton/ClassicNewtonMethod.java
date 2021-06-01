package com.mygdx.newton;

import com.mygdx.nmethods.AbstractNMethod;
import com.mygdx.nmethods.Value;
import com.mygdx.nmethods.Vector;

import java.util.List;

public class ClassicNewtonMethod<F extends SolverQuadraticFunction> extends AbstractNMethod<F> {

    public ClassicNewtonMethod(final F f) {
        super(f);
    }

    @Override
    public Value<Vector, Double> nextIteration(final Value<Vector, Double> x, final double eps) {
        final Vector gradient = getFunction().gradient(x.getValue());
        if (gradient.length() < eps) {
            return null;
        }
        final List<Double> p = getFunction().getSolver().solve(gradient.multiply(-1));
        System.out.println(x.getValue().toString() + " " + new Vector(p).length());
        if (new Vector(p).length() < eps) {
            return null;
        }
        return new Value<>(x.getValue().add(p), getFunction());
    }

}
