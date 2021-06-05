package com.mygdx.newton;

import com.mygdx.linear.Matrix;
import com.mygdx.linear.SystemSolveMatrix;
import com.mygdx.nmethods.AbstractNMethod;
import com.mygdx.nmethods.Value;
import com.mygdx.nmethods.Vector;

import java.util.List;
import java.util.stream.IntStream;

public class MarquardtMethodFirst<F extends NewtonFunction> extends AbstractNMethod<F> {

    private double tau0;
    private double beta;

    public MarquardtMethodFirst(F function) {
        super(function);
    }

    @Override
    public Value<Vector, Double> nextIteration(Value<Vector, Double> x, double eps) {
        Vector gradient = getFunction().gradient(x.getValue());
        SystemSolveMatrix hesse = getFunction().hesse(x.getValue());
        double tau = tau0;
        Vector p;
        Value<Vector, Double> y;
        do {
            final double fTau = tau;
            IntStream.range(0, hesse.nColumns()).forEach(i ->
                    hesse.get(i, i).set(hesse.get(i, i).get() + fTau));
            p = new Vector(hesse.solve(gradient.multiply(-1)));
            y = new Value<>(x.getValue().add(p), getFunction());
            tau /= beta;
        } while (y.getFValue() > x.getFValue());
        tau0 = tau0 * beta;
        if (p.length() < eps) {
            return null;
        }
        return y;
    }

    @Override
    public Vector findMin(double eps) {
        tau0 = 1;
        beta = 0.5;
        return super.findMin(eps);
    }
}
