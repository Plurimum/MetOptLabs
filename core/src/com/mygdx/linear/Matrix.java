package com.mygdx.linear;

public interface Matrix {

    MatrixElement get(int x, int y);

    int nRows();

    int nColumns();

    default Matrix multiply(final Matrix other) {
        final ArrayMatrix result = new ArrayMatrix(nRows(), other.nColumns());
        for (int i = 0; i < nRows(); i++) {
            for (int j = 0; j < other.nColumns(); j++) {
                for (int k = 0; k < nColumns(); k++) {
                    result.get(i, j).set(result.get(i, j).get() + get(i, k).get() * other.get(k, j).get());
                }
            }
        }
        return result;
    }

    default Matrix subtract(final Matrix other) {
        final ArrayMatrix result = new ArrayMatrix(nRows(), nColumns());
        for (int i = 0; i < nRows(); i++) {
            for (int j = 0; j < other.nColumns(); j++) {
                result.get(i, j).set(get(i, j).get() - other.get(i, j).get());
            }
        }
        return result;
    }

}
