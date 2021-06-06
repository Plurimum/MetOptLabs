package com.mygdx.newton;

import com.mygdx.linear.ArrayMatrix;
import com.mygdx.linear.Matrix;
import com.mygdx.linear.SystemSolveMatrix;
import com.mygdx.nmethods.AbstractNMethod;
import com.mygdx.nmethods.DiagonalMatrixImpl;
import com.mygdx.nmethods.Value;
import com.mygdx.nmethods.Vector;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MarquardtMethodFirst<F extends NewtonFunction> extends AbstractNMethod<F> {

    private double tau0;
    private double beta;

    public MarquardtMethodFirst(F function) {
        super(function);
    }
    public MarquardtMethodFirst(F function, final Vector start) {
        super(function, start);
    }

    @Override
    public Value<Vector, Double> nextIteration(Value<Vector, Double> x, double eps) {
        Vector gradient = getFunction().gradient(x.getValue());
        SystemSolveMatrix hesse = getFunction().hesse(x.getValue());
        double tau = tau0;
        Vector p;
        Value<Vector, Double> y;
        //System.out.println(tau);
        do {
            SystemSolveMatrix sum = hesse.add(new ArrayMatrix(new DiagonalMatrixImpl(Collections.nCopies(hesse.nColumns(), tau))));
            p = new Vector(sum.solve(gradient.multiply(-1)));
            y = new Value<>(x.getValue().add(p), getFunction());
            tau /= beta;
        } while (y.getFValue() > x.getFValue());
        tau0 = tau0 * beta * beta;
        System.out.println(y.getValue());
        if (p.length() < eps) {
            return null;
        }
        return y;
    }

    @Override
    public Vector findMin(double eps) {
        tau0 = 1e+4;
        beta = 0.5;
        return super.findMin(eps);
    }
}
