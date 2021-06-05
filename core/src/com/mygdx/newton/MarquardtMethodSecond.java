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
    public MarquardtMethodSecond(F function) {
        super(function);
    }

    @Override
    public Value<Vector, Double> nextIteration(Value<Vector, Double> x, double eps) {
        Vector gradient = getFunction().gradient(x.getValue());
        SystemSolveMatrix hesse = getFunction().hesse(x.getValue());
        double tau = findTau(hesse);
        System.out.println(tau);
        IntStream.range(0, hesse.nColumns()).forEach(i -> hesse.get(i, i).set(hesse.get(i, i).get() + tau));
        Vector p = new Vector(hesse.solve(gradient.multiply(-1)));
        if (p.length() < eps) {
            return null;
        }
        return new Value<>(x.getValue().add(p), getFunction());
    }

    private double findTau(final SystemSolveMatrix hesse) {
        double tau = 0.;
        final int size = hesse.nColumns();
        Matrix result;
        CholeskyDecomposition decomposition;
        final Matrix identityMatrix = new ArrayMatrix(new DiagonalMatrixImpl(Collections.nCopies(size, 1.)));
        while (true) {
            result = hesse.add(identityMatrix.multiply(tau));
            decomposition = new CholeskyDecomposition(result);
            if (decomposition.getL().multiply(decomposition.getTransposedL()).equals(result, 1e-7)) {
                return tau;
            }
            tau = Math.max(1., 2 * tau);
        }
    }
}
