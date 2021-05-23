package com.mygdx.nmethods;

import java.util.List;

public class QuadraticFunction implements NFunction {

    private final int n;
    public final Matrix a;
    public final Vector b;
    public final double c;

    public QuadraticFunction(final Matrix a, List<Double> b, final double c) {
        this.n = b.size();
        this.a = a;
        this.b = new Vector(b);
        this.c = c;
    }

    public QuadraticFunction(final List<List<Double>> a, List<Double> b, final double c) {
        this(new Matrix(a), b, c);
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

}
