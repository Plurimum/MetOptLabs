package com.mygdx.newton;

import com.mygdx.nmethods.DoubleMatrix;
import com.mygdx.nmethods.Vector;

public class PowellMethod<F extends NewtonFunction> extends AbstractQuasiNewton<F>{
    public PowellMethod(F function) {
        super(function);
    }

    @Override
    protected DoubleMatrix nextG() {
        final Vector wavyDeltaX = prevDeltaX.add(g.multiply(prevDeltaW));
        final DoubleMatrix summand = wavyDeltaX.multiply(
                new Vector(wavyDeltaX))
                .multiply(1 / wavyDeltaX.scalarProduct(prevDeltaW));
        return g.add(summand.multiply(-1));
    }
}
