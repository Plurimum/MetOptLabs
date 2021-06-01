package com.mygdx.newton;

import com.mygdx.linear.Matrix;
import com.mygdx.nmethods.DoubleMatrix;
import com.mygdx.nmethods.Vector;

import java.util.AbstractList;

public class MatrixImplProxy<M extends Matrix> extends AbstractList<Vector> implements DoubleMatrix {

    final private M matrix;

    public MatrixImplProxy(final M matrix) {
        this.matrix = matrix;
    }

    @Override
    public double get(final int r, final int c) {
        return matrix.get(r, c).get();
    }

    @Override
    public Vector get(final int index) {
        throw new UnsupportedOperationException("get(index)");
    }

    @Override
    public int size() {
        return matrix.nRows();
    }

    public M getMatrix() {
        return matrix;
    }
}
