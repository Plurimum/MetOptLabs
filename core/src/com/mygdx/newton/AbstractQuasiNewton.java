package com.mygdx.newton;

import com.mygdx.methods.GoldenSectionMethod;
import com.mygdx.methods.Method;
import com.mygdx.nmethods.*;

import java.util.Collections;
import java.util.function.Function;

public abstract class AbstractQuasiNewton<F extends NFunction> extends AbstractFunctionNMethod<F> {

    protected final Function<Function<Double, Double>, Method> methodFactory = GoldenSectionMethod::new;

    protected AbstractQuasiNewton(final F function) {
        super(function);
    }

    protected AbstractQuasiNewton(final F function, final Vector start) {
        super(function, start);
    }

    protected abstract DoubleMatrix nextG(final DoubleMatrix g, final Vector deltaX, final Vector deltaW);

    protected Vector nextX(final Vector x, final Vector p, final double eps) {
        final Function<Double, Vector> f = t -> x.add(p.multiply(t));
        final double alpha = methodFactory.apply(getFunction().compose(f)).findMin(0., 10., eps);
        return f.apply(alpha);
    }

    @Override
    public Vector findMin(final double eps) {
        Value<Vector, Vector> x = new Value<>(getStart(), getFunction()::gradient);
        while (true) {
            DoubleMatrix g = new DiagonalMatrixImpl(Collections.nCopies(getFunction().getN(), 1.));
            for (int i = 0; i < getFunction().getN(); i++) {
                System.out.println(x.getValue());
                final Vector p = g.multiply(x.getFValue()).multiply(-1);
                final Value<Vector, Vector> next = new Value<>(nextX(x.getValue(), p, eps), getFunction()::gradient);
                final Vector deltaX = next.getValue().add(x.getValue().multiply(-1));
                if (deltaX.length() < eps) {
                    return next.getValue();
                }
                g = nextG(g, deltaX, next.getFValue().add(x.getFValue().multiply(-1)));
                x = next;
            }
        }
    }
}
