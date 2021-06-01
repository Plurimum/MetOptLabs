package com.mygdx.nmethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface DoubleMatrix extends List<Vector> {

    double get(final int r, final int c);

    @Override
    Vector get(final int index);

    @Override
    int size();

    default Vector multiply(final Vector other) {
        List<Double> result = new ArrayList<>(Collections.nCopies(size(), 0.));
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                result.set(j, result.get(j) + get(i, j) * other.get(j));
            }
        }
        return new Vector(result);
    }
}
