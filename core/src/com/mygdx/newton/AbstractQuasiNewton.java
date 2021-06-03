package com.mygdx.newton;

import com.mygdx.methods.GoldenSectionMethod;
import com.mygdx.methods.Method;
import com.mygdx.nmethods.*;

import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class AbstractQuasiNewton<F extends NFunction> extends AbstractNMethod<F> {

    protected final Function<Function<Double, Double>, Method> methodFactory;
    protected DoubleMatrix g;
    protected Vector prevDeltaX;
    protected Vector prevDeltaW;

    // надо что-то решить с тем, что мне нужна первоначальная инициализация от чего-то (желательно от х = {0, 0, ...}),
    // чтобы nextIteration от него отталкивался
    // тогда кажется, должно работать
    protected AbstractQuasiNewton(F function) {
        super(function);
        this.methodFactory = GoldenSectionMethod::new;
    }

    protected abstract DoubleMatrix nextG();

    @Override
    public Value<Vector, Double> nextIteration(Value<Vector, Double> x, double eps) {
        Vector wk = getFunction().gradient(x.getValue()).multiply(-1);
        g = nextG();
        prevDeltaW = wk.add(prevDeltaW.multiply(-1));
        Vector p = g.multiply(wk);
        final Function<Double, Vector> func = t -> x.getValue().add(p.multiply(t));
        final double alpha = methodFactory.apply(getFunction().compose(func)).findMin(0.5, 1.5, eps);
        final Vector pa = p.multiply(alpha);
        prevDeltaX = x.getValue().add(pa).add(prevDeltaX.multiply(-1));
        if (prevDeltaX.length() < eps) {
            return null;
        }
        return new Value<>(x.getValue().add(pa), getFunction());
    }

    @Override
    public Vector findMin(final double eps) {
        final int size = getFunction().getN();
        this.g = new MatrixImpl(IntStream.range(0, size)
                .mapToObj(i -> IntStream.range(0, size)
                        .mapToObj(j -> i == j ? 1. : 0.)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList()));
        final Vector x = new Vector(Collections.nCopies(size, 0.));
        this.prevDeltaW = getFunction().gradient(x).multiply(-1);
        final Vector p = prevDeltaW;
        final Function<Double, Vector> func = t -> x.add(p.multiply(t));
        final double alpha = methodFactory.apply(getFunction().compose(func)).findMin(0.5, 1.5, eps);
        final Vector pa = p.multiply(alpha);
        prevDeltaX = x.add(pa);
        return super.findMin(eps);
    }
}
