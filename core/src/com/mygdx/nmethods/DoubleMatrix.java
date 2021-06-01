package com.mygdx.nmethods;

import java.util.List;

public interface DoubleMatrix extends List<Vector> {

    double get(final int r, final int c);

    @Override
    Vector get(final int index);

    @Override
    int size();

    default Vector multiply(final Vector other) {
        Vector result = new Vector();
        for (Vector doubles : this) {
            result.add(doubles.scalarProduct(other));
        }
        return result;
    }
}
