package com.mygdx.newton;

import com.mygdx.linear.ArrayMatrix;
import com.mygdx.linear.Matrix;
import com.mygdx.linear.SystemSolveMatrix;
import com.mygdx.nmethods.AbstractNMethod;
import com.mygdx.nmethods.DiagonalMatrixImpl;
import com.mygdx.nmethods.Value;
import com.mygdx.nmethods.Vector;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MarquardtMethodSecond<F extends NewtonFunction> extends AbstractNMethod<F> {

    private double tau;

    public MarquardtMethodSecond(F function) {
        super(function);
    }

    public MarquardtMethodSecond(F function, final Vector start) {
        super(function, start);
    }

    @Override
    public Value<Vector, Double> nextIteration(final Value<Vector, Double> x, final double eps) {
        final Vector gradient = getFunction().gradient(x.getValue());
        final SystemSolveMatrix hesse = getFunction().hesse(x.getValue());
        CholeskyDecomposition decomposition;

        final int size = hesse.nColumns();
        final Matrix identityMatrix = new ArrayMatrix(new DiagonalMatrixImpl(Collections.nCopies(size, 1.)));
        while (true) {
            final Matrix result = hesse.add(identityMatrix.multiply(tau));
            decomposition = new CholeskyDecomposition(result);
            if (decomposition.getL().multiply(decomposition.getTransposedL()).equals(result, eps)) {
                tau /= 2;
                break;
            }
            tau = Math.max(1., 2 * tau);
        }
        final Vector p = new Vector(decomposition.getL().solve(gradient.multiply(-1)));
        System.out.println(x.getValue());
        if (p.length() < eps) {
            return null;
        }
        return new Value<>(x.getValue().add(p), getFunction());
    }

    public Vector findMin(final double eps) {
        tau = 0.;
        return super.findMin(eps);
    }
}
