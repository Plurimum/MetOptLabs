package com.mygdx.linear.test;

import java.util.*;
import java.util.stream.*;

import com.mygdx.linear.*;
import com.mygdx.nmethods.Vector;

public class Research {

    private static TestGenerator gen = new TestGenerator(null);

    private static void research(SystemSolveMatrix solver, int n, int k) {
        final List<Double> correct = IntStream.range(1, n + 1).asDoubleStream().boxed().collect(Collectors.toList());
        final Matrix temp = solver.multiply(new SingleColumnMatrix(correct));
        final List<Double> f = IntStream.range(0, n).mapToDouble(i -> temp.get(i, 0).get()).boxed().collect(Collectors.toList());
        MatrixElementImpl.resetCounter();
        final List<Double> xk = solver.solve(f);
        int count = MatrixElementImpl.getCounter();
        Vector v = new Vector(correct);
        final double diff = v.add(new Vector(xk).multiply(-1)).length();
        final double diffD = diff / v.length();
        if (Double.isNaN(diff) || Double.isInfinite(diff)) {
            System.out.printf("%d %d No solution No solution %d%n", n, k, count);
        } else {
            System.out.printf("%d %d %1.7e %1.7e %d %n", n, k, diff, diffD, count);
        }
    }

    public static void main(String[] args) {
        System.out.println("Gauss");
        for (int n = 10; n <= 1000; n *= 10) {
            for (int k = 1; k <= 15; ++k) {
                research(new ArrayMatrix(gen.generateDiagonallyDominant(n, k)), n, k);
            }
        }
        System.out.println("LU");
        for (int n = 10; n <= 1000; n *= 10) {
            for (int k = 1; k <= 15; ++k) {
                research(gen.generateDiagonallyDominant(n, k), n, k);
            }
        }
    }
}
