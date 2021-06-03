package com.mygdx.newton;

import com.mygdx.nmethods.DoubleMatrix;
import com.mygdx.nmethods.Vector;

public class BFShMethod<F extends SolverQuadraticFunction> extends AbstractQuasiNewton<F>{
    public BFShMethod(F function) {
        super(function);
    }

    @Override
    protected DoubleMatrix nextG() {
        final double scalarRo = g.multiply(prevDeltaW).scalarProduct(prevDeltaW);
        final Vector vectorR = g.multiply(prevDeltaW).multiply(1 / scalarRo)
                .add(prevDeltaX.multiply(-1 / prevDeltaX.scalarProduct(prevDeltaW)));
        final DoubleMatrix firstSummand = prevDeltaX.multiply(new Vector(prevDeltaX))
                .multiply(1 / prevDeltaW.scalarProduct(prevDeltaX));
        final DoubleMatrix secondSummand = g.multiply(prevDeltaW)
                .multiply(prevDeltaW)
                .multiply(g.transposed())
                .multiply(1 / scalarRo);
        final DoubleMatrix thirdSummand = vectorR.multiply(scalarRo).multiply(vectorR);
        return g.add(firstSummand.multiply(-1)).add(secondSummand.multiply(-1)).add(thirdSummand);
    }
}
