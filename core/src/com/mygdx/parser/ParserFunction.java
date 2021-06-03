package com.mygdx.parser;

import com.mygdx.nmethods.QuadraticFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParserFunction extends QuadraticFunction {
    public ParserFunction(List<List<Double>> a, List<Double> b, double c) {
        super(a, b, c);
    }

    public ParserFunction(int n, double c) {
        super(Collections.nCopies(n, Collections.nCopies(n, 0.)), Collections.nCopies(n, 0.), c);
    }

    public ParserFunction(int n, String var) {
        super(Collections.nCopies(n, Collections.nCopies(n, 0.)), var.equals("x") ? Arrays.asList(1., 0.) :
                Arrays.asList(0., 1.), 0);

    }

    public ParserFunction add(final QuadraticFunction other) {
        final List<List<Double>> ra = new ArrayList<>();
        IntStream.range(0, getN()).forEach(i -> ra.add(i, getA().get(i).add(other.getA().get(i))));
        return new ParserFunction(ra, getB().add(other.getB()), getC() + other.getC());
    }

    public ParserFunction multiply(final QuadraticFunction other) {
        final List<List<Double>> ra = new ArrayList<>();
        ra.add(Arrays.asList(2 * getB().get(0) * other.getB().get(0),
                other.getB().get(0) * getB().get(1) + other.getB().get(1) * getB().get(0)));

        ra.add(Arrays.asList(other.getB().get(0) * getB().get(1) + other.getB().get(1) * getB().get(0),
                2 * getB().get(1) * other.getB().get(1)));

        return new ParserFunction(ra, getB().multiply(other.getC()).add(other.getB().multiply(getC())),
                other.getC() * getC());
    }

    public ParserFunction negative() {
        return new ParserFunction(
                this.getA().stream().map(list ->
                        list.stream().map(val -> -val).collect(Collectors.toList())
                ).collect(Collectors.toList()),
                getB().stream().map(val -> -val).collect(Collectors.toList()),
                -getC());
    }
}
