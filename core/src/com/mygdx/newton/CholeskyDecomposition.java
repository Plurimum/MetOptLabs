package com.mygdx.newton;

import com.mygdx.linear.ArrayMatrix;
import com.mygdx.linear.Matrix;
import com.mygdx.linear.MatrixElement;
import com.mygdx.linear.SystemSolveMatrix;

import java.util.ArrayList;
import java.util.List;

public class CholeskyDecomposition {

    private final SystemSolveMatrix l;

    public CholeskyDecomposition(final Matrix a) {
        l = new CholeskyMatrix(a.nRows(), a.nColumns());
        for (int i = 0; i < a.nRows(); i++) {
            for (int j = 0; j <= i; ++j) {
                double sum = 0;
                for (int k = 0; k < j; k++) {
                    sum += l.get(i, k).get() * l.get(j, k).get();
                }
                if (i == j) {
                    l.get(i, j).set(Math.sqrt(a.get(i, i).get() - sum));
                } else {
                    l.get(i, j).set((a.get(i, j).get() - sum) / l.get(j, j).get());
                }
            }
        }
    }

    public SystemSolveMatrix getL() {
        return l;
    }

    public Matrix getTransposedL() {
        return new Matrix() {
            @Override
            public MatrixElement get(final int r, final int c) {
                return l.get(c, r);
            }

            @Override
            public int nRows() {
                return l.nColumns();
            }

            @Override
            public int nColumns() {
                return l.nRows();
            }
        };
    }

    private static class CholeskyMatrix extends ArrayMatrix {

        public CholeskyMatrix(int rows, int columns) {
            super(rows, columns);
        }

        @Override
        public List<Double> solve(final List<Double> free) {
            final List<Double> result = new ArrayList<>(free);
            for (int i = 0; i < nRows(); i++) {
                for (int j = 0; j < i; j++) {
                    result.set(i, result.get(i) - result.get(j) * get(i, j).get());
                }
                result.set(i, result.get(i) / get(i, i).get());
            }

            for (int i = nColumns() - 1; i >= 0; i--) {
                for (int j = nColumns() - 1; j > i; j--) {
                    result.set(i,result.get(i) - result.get(j) * get(j, i).get());
                }
                result.set(i, result.get(i) / get(i, i).get());
            }
            return result;
        }
    }
}
