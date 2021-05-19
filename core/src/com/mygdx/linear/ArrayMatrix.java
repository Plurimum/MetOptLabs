package com.mygdx.linear;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.IntStream;

public class ArrayMatrix implements Matrix {

    private static final double EPS = 1e-15;
    private final List<List<MatrixElement>> matrix;

    public ArrayMatrix(int rows, int columns) {
        matrix = Stream.generate(() ->
                Stream.generate(() ->
                        (MatrixElement) new MatrixElementImpl()
                ).limit(columns).collect(Collectors.toList())
        ).limit(rows).collect(Collectors.toList());
    }

    public ArrayMatrix(List<List<MatrixElement>> matrix) {
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


    public static void addVec(final Matrix m, final int row, final MatrixElement coeff, final List<MatrixElement> vec) {
        IntStream.range(0, m.nColumns()).forEach(col -> m.get(row, col).set(m.get(row, col).get() + vec.get(col).get() * coeff.get()));
    }

    public static void mulVec(final Matrix m, final int row, final MatrixElement coeff) {
        IntStream.range(0, m.nColumns()).forEach(col -> m.get(row, col).set(m.get(row, col).get() * coeff.get()));
    }

    public static List<Double> solveSystem(final Matrix a, final List<Double> b) {
        assert a.nRows() == b.size();
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
                final int iInd = i;
                final int swapInd = ind;
                List<MatrixElement> tmp = IntStream.range(0, a.nColumns()).mapToObj(
                        c -> (MatrixElement) new MatrixElementImpl(a.get(iInd, c).get() - a.get(swapInd, c).get()))
                        .collect(Collectors.toList());
                MatrixElement one = new MatrixElementImpl(-1);
                addVec(a, iInd, one, tmp);
                one.set(1);
                addVec(a, swapInd, one, tmp);
            }
            MatrixElement d = new MatrixElementImpl(1 / aii);
            mulVec(a, i, d);
            b.set(i, b.get(i) * d.get());
            for (int j = 0; j < a.nRows(); j++) {
                if (i != j) {
                    MatrixElement coeff = a.get(i, j);
                    coeff.set(-coeff.get());
                    final int row = i;
                    addVec(a, j, coeff, IntStream.range(0, a.nColumns()).mapToObj(col -> a.get(row, col)).collect(Collectors.toList()));
                    b.set(j, b.get(j) + coeff.get() * b.get(i));
                }
            }
        }
        return b;
    }


}
