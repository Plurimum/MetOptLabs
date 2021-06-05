package com.mygdx.newton;

import com.mygdx.nmethods.DoubleMatrix;
import com.mygdx.nmethods.Vector;

public class PowellMethod<F extends NewtonFunction> extends AbstractQuasiNewton<F>{
    public PowellMethod(F function) {
        super(function);
    }

    public PowellMethod(F function, Vector start) {
        super(function, start);
    }
    @Override
    protected DoubleMatrix nextG(final DoubleMatrix g, final Vector deltaX, final Vector deltaW) {
        final Vector wavyDeltaX = deltaX.add(g.multiply(deltaW));
        final DoubleMatrix summand = wavyDeltaX.multiply(
                new Vector(wavyDeltaX))
                .multiply(1 / wavyDeltaX.scalarProduct(deltaW));
        return g.add(summand.multiply(-1));
    }
}
