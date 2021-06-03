package com.mygdx.newton;

import com.mygdx.methods.GoldenSectionMethod;
import com.mygdx.methods.Method;
import com.mygdx.nmethods.AbstractNMethod;
import com.mygdx.nmethods.Value;
import com.mygdx.nmethods.Vector;

import java.util.function.Function;

public class DescentDirectNewton <F extends SolverQuadraticFunction> extends OptimizedNewton<F> {

    public DescentDirectNewton(F function, Function<Function<Double, Double>, Method> methodFactory) {
        super(function, methodFactory);
    }

    public DescentDirectNewton(final F function, final Vector start) {
        super(function, start);
    }

    @Override
    public Value<Vector, Double> nextIteration(Value<Vector, Double> x, double eps) {
        final Vector gradient = getFunction().gradient(x.getValue());
        Vector p = new Vector(getFunction().getSolver().solve(gradient.multiply(-1)));
        if (p.scalarProduct(gradient) > 0) {
            p = gradient.multiply(-1);
        }
        final Vector finalP = p;
        final Function<Double, Vector> func = t -> x.getValue().add(finalP.multiply(t));
        final double alpha = methodFactory.apply(getFunction().compose(func)).findMin(0.5, 1.5, eps);
        final Vector pa = finalP.multiply(alpha);
        if (pa.length() < eps) {
            return null;
        }
        return new Value<>(x.getValue().add(pa), getFunction());
    }
}
