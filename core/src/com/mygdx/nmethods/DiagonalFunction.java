package com.mygdx.nmethods;

import java.util.List;

public class DiagonalFunction extends QuadraticFunction {

    public DiagonalFunction(List<Double> a, List<Double> b, double c) {
        super(new DiagonalMatrixImpl(a), b, c);
    }

    @Override
    public Vector gradient(Vector point) {
        Vector result = new Vector();
        for (int i = 0; i < point.size(); i++) {
            result.add(point.get(i) * getA().get(i, i) + getB().get(i));
        }
        return result;
    }

    @Override
    public Double apply(Vector arg) {
        double result = 0.;
        for (int i = 0; i < arg.size(); i++) {
            result += arg.get(i) * arg.get(i) * getA().get(i, i) / 2 + arg.get(i) * getB().get(i);
        }
        result += getC();
        return result;
    }
}
