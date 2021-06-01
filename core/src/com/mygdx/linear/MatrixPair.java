package com.mygdx.linear;

public class MatrixPair {
    private final ProfileMatrix matrix;

    public MatrixPair(ProfileMatrix matrix) {
        this.matrix = matrix;
    }

    private abstract class CommonMatrix implements Matrix {

        @Override
        public int nRows() {
            return matrix.nRows();
        }

        @Override
        public int nColumns() {
            return matrix.nColumns();
        }
    }

    public Matrix getL() {
        return new CommonMatrix() {
            @Override
            public MatrixElement get(int x, int y) {
                if (x < y) {
                    return new UnmodifiableMatrixElement(0);
                } else {
                    return new UnmodifiableMatrixElement(matrix.get(x, y).get());
                }
            }
        };
    }

    public Matrix getU() {
        return new CommonMatrix() {
            @Override
            public MatrixElement get(int x, int y) {
                if (x > y) {
                    return new UnmodifiableMatrixElement(0);
                } else if (x == y) {
                    return new UnmodifiableMatrixElement(1);
                } else {
                    return new UnmodifiableMatrixElement(matrix.get(x, y).get());
                }
            }
        };
    }
}
