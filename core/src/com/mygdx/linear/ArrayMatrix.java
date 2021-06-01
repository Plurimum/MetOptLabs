package com.mygdx.linear;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.IntStream;

public class ArrayMatrix implements SystemSolveMatrix {

    private static final double EPS = 1e-15;
    private final List<List<MatrixElementImpl>> matrix;

    public ArrayMatrix(int rows, int columns) {
        matrix = Stream.generate(() ->
                Stream.generate(MatrixElementImpl::new).limit(columns).collect(Collectors.toList())
        ).limit(rows).collect(Collectors.toList());
    }

    public ArrayMatrix(final List<List<MatrixElementImpl>> matrix) {
        this.matrix = matrix;
    }

    public ArrayMatrix(final Matrix m) {
        matrix = Stream.generate(() ->
                Stream.generate(MatrixElementImpl::new).limit(m.nColumns()).collect(Collectors.toList())
        ).limit(m.nRows()).collect(Collectors.toList());
        for (int i = 0; i < m.nRows(); i++) {
            for (int j = 0; j < m.nColumns(); j++) {
                get(i, j).set(m.get(i, j).get());
            }
        }
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

    private void addVec(final int row, final double coefficient, final List<? extends MatrixElement> vec) {
        IntStream.range(0, nColumns()).forEach(col ->
                get(row, col).set(get(row, col).get() + (vec.get(col).get() * coefficient)));
    }

    private void mulVec(final int row, final MatrixElementImpl coeff) {
        IntStream.range(0, nColumns()).forEach(col -> get(row, col).set(get(row, col).get() * coeff.get()));
    }

    @Override
    public String toString() {
        StringBuilder r = new StringBuilder();
        for (int row = 0; row < nRows(); row++) {
            for (int col = 0; col < nColumns(); col++) {
                r.append(get(row, col).get()).append(" ");
            }
            r.append(System.lineSeparator());
        }
        return r.toString();
    }

    @Override
    public List<Double> solve(final List<Double> free) {
        List<Double> result = new ArrayList<>(free);
        for (int i = 0; i < nRows(); i++) {
            int indMax = 0;
            for (int r = 0; r < nRows(); r++) {
                if (Math.abs(get(indMax, i).get()) < Math.abs(get(r, i).get())) {
                    indMax = r;
                }
            }
            if (Math.abs(get(indMax, i).get()) < EPS) {
                throw new IllegalStateException("Many solutions");
            }
            Collections.swap(matrix, i, indMax);
            Collections.swap(result, i, indMax);

            double aii = get(i, i).get();
            MatrixElementImpl d = new MatrixElementImpl(1 / aii);
            mulVec(i, d);
            result.set(i, result.get(i) * d.get());
            List<MatrixElementImpl> vec = matrix.get(i);
            for (int j = 0; j < nRows(); j++) {
                if (i != j) {
                    final double coefficient = -get(j, i).get();
                    addVec(j, coefficient, vec);
                    result.set(j, result.get(j) + coefficient * result.get(i));
                }
            }
        }
        return result;
    }
}
