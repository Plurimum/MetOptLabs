package com.mygdx.nmethods;

import java.util.List;

public class QuadraticFunction implements NFunction {

    private final int n;
    private final DoubleMatrix a;
    private final Vector b;
    private final double c;

    public QuadraticFunction(final DoubleMatrix a, List<Double> b, final double c) {
        this.n = b.size();
        this.a = a;
        this.b = new Vector(b);
        this.c = c;
    }

    public QuadraticFunction(final List<List<Double>> a, List<Double> b, final double c) {
        this(new MatrixImpl(a), b, c);
    }

    @Override
    public Double apply(final Vector arg) {
        return arg.scalarProduct(a.multiply(arg)) / 2 + arg.scalarProduct(b) + c;
    }

    @Override
    public Vector gradient(Vector point) {
        return a.multiply(point).add(b);
    }

    @Override
    public int getN() {
        return n;
    }

    public DoubleMatrix getA() {
        return a;
    }

    public Vector getB() {
        return b;
    }

    public double getC() {
        return c;
    }
}
