package com.mygdx.newton;

import com.mygdx.nmethods.DoubleMatrix;
import com.mygdx.nmethods.NFunction;
import com.mygdx.nmethods.Vector;

public class BFShMethod<F extends NFunction> extends AbstractQuasiNewton<F>{

    public BFShMethod(F function) {
        super(function);
    }

    @Override
    protected DoubleMatrix nextG(final DoubleMatrix g, final Vector deltaX, final Vector deltaW) {
        final double scalarRo = g.multiply(deltaW).scalarProduct(deltaW);

        final Vector vectorR = g.multiply(deltaW).multiply(1 / scalarRo)
                .add(deltaX.multiply(-1 / deltaX.scalarProduct(deltaW)));
        final DoubleMatrix firstSummand = deltaX.multiply(deltaX)
                .multiply(1 / deltaW.scalarProduct(deltaX));
        final DoubleMatrix secondSummand = g.multiply(deltaW)
                .multiply(deltaW)
                .multiply(g.transposed())
                .multiply(1 / scalarRo);
        final DoubleMatrix thirdSummand = vectorR.multiply(scalarRo).multiply(vectorR);
        return g.add(firstSummand.multiply(-1)).add(secondSummand.multiply(-1)).add(thirdSummand);
    }
}
