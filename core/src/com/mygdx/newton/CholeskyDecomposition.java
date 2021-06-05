package com.mygdx.newton;

import com.mygdx.linear.ArrayMatrix;
import com.mygdx.linear.Matrix;
import com.mygdx.linear.MatrixElement;

public class CholeskyDecomposition {

    private final Matrix l;

    public CholeskyDecomposition(final Matrix a) {
        l = new ArrayMatrix(a.nRows(), a.nColumns());
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

    public Matrix getL() {
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
}
