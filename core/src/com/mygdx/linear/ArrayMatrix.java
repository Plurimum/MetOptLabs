package com.mygdx.linear;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.IntStream;

public class ArrayMatrix implements Matrix {

    private static final double EPS = 1e-15;
    private final List<List<MatrixElementImpl>> matrix;

    public ArrayMatrix(int rows, int columns) {
        matrix = Stream.generate(() ->
                Stream.generate(MatrixElementImpl::new).limit(columns).collect(Collectors.toList())
        ).limit(rows).collect(Collectors.toList());
    }

    public ArrayMatrix(List<List<MatrixElementImpl>> matrix) {
        this.matrix = matrix;
    }

    @Override
    public MatrixElement get(int x, int y) {
        return matrix.get(x).get(y);
    }

    @Override
    public int nRows() {
        return matrix.size();
    }

    @Override
    public int nColumns() {
        return matrix.size() == 0 ? 0 : matrix.get(0).size();
    }


    public static void addVec(final ArrayMatrix m, final int row, final MatrixElementImpl coeff, final List<MatrixElementImpl> vec) {
        IntStream.range(0, m.nColumns()).forEach(col ->
                m.get(row, col).set(m.get(row, col).get() + (vec.get(col).get() * coeff.get())));
    }

    public static void mulVec(final ArrayMatrix m, final int row, final MatrixElementImpl coeff) {
        IntStream.range(0, m.nColumns()).forEach(col -> m.get(row, col).set(m.get(row, col).get() * coeff.get()));
    }

    public static void printSys(final ArrayMatrix a, final List<Double> b) {
        IntStream.range(0, a.nRows()).forEach(rov -> {
            IntStream.range(0, a.nColumns()).forEach(colum -> System.out.print(a.get(rov, colum).get() + " "));
            System.out.println(b.get(rov));
        });
    }

    public static List<Double> solveSystem(final ArrayMatrix a, final List<Double> b) {
        for (int i = 0; i < a.nRows(); i++) {
            double aii = a.get(i, i).get();
            if (Math.abs(aii) < EPS) {
                int ind = a.nRows() - 1;
                while (ind > i && Math.abs(a.get(ind, i).get()) < EPS) {
                    ind--;
                }
                if (ind == i) {
                    // infinitely many solutions
                    return null;
                }
                Collections.swap(a.matrix, i, ind);
            }
            MatrixElementImpl d = new MatrixElementImpl(1 / aii);
            mulVec(a, i, d);
            b.set(i, b.get(i) * d.get());
            List<MatrixElementImpl> vec = a.matrix.get(i);
            for (int j = 0; j < a.nRows(); j++) {
                if (i != j) {
                    MatrixElementImpl coeff = new MatrixElementImpl(-a.get(j, i).get());
                    addVec(a, j, coeff, vec);
                    b.set(j, b.get(j) + coeff.get() * b.get(i));
                }
            }
        }
        return b;
    }


}
