package com.mygdx.nmethods;

import java.util.Collections;

public class GradientMethod extends QuadraticMethod {

    public GradientMethod(final QuadraticFunction f) {
        super(f);
    }

    @Override
    public Vector findMin(final double eps) {
        Value<Vector, Double> x = new Value<>(new Vector(Collections.nCopies(f.n, 0.)), f);
        Vector gradX;
        double gradientDistance;
        double alpha = 100.;
        while ((gradientDistance = (gradX = f.gradient(x.getValue())).dist()) > eps) {
            while (true) {
                Value<Vector, Double> y = new Value<>(
                        MatrixAlgebra.sum(
                                x.getValue(),
                                MatrixAlgebra.multiply(gradX, -alpha / gradientDistance)).force(), f);
                if (y.getFValue() < x.getFValue()) {
                    x = y;
                    break;
                } else {
                    alpha /= 2;
                }
            }
        }
        return x.getValue();
    }
}
