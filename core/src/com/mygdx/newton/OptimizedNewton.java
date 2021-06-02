package com.mygdx.newton;

import com.mygdx.methods.GoldenSectionMethod;
import com.mygdx.methods.Method;
import com.mygdx.nmethods.AbstractNMethod;
import com.mygdx.nmethods.Value;
import com.mygdx.nmethods.Vector;

import java.util.function.Function;

public class OptimizedNewton <F extends SolverQuadraticFunction> extends AbstractNMethod<F> {

    private final Function<Function<Double, Double>, Method> methodFactory;

    public OptimizedNewton(final F function, final Function<Function<Double, Double>, Method> methodFactory) {
        super(function);
        this.methodFactory = methodFactory;
    }

    public OptimizedNewton(final F function, final Vector start) {
        super(function, start);
        this.methodFactory = GoldenSectionMethod::new;
    }

    @Override
    public Value<Vector, Double> nextIteration(final Value<Vector, Double> x, final double eps) {
        final Vector gradient = getFunction().gradient(x.getValue());
        final Vector p = new Vector(getFunction().getSolver().solve(gradient.multiply(-1)));
        final Function<Double, Vector> func = t -> x.getValue().add(p.multiply(t));
        final double alpha = methodFactory.apply(getFunction().compose(func)).findMin(0.5, 1.5, eps);
        final Vector pa = p.multiply(alpha);
        if (pa.length() < eps) {
            return null;
        }
        return new Value<>(x.getValue().add(pa), getFunction());


    }
}
