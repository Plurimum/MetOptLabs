package com.mygdx.linear;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Vector implements Matrix {

    private final List<MatrixElement> vector;

    public Vector(final List<Double> list) {
        vector = list.stream().map(MatrixElementImpl::new).collect(Collectors.toList());
    }

    @Override
    public MatrixElement get(int x, int y) {
        if (y >= 1) {
            throw new IndexOutOfBoundsException("Стасян)");
        }
        return vector.get(x);
    }

    @Override
    public int nRows() {
        return vector.size();
    }

    @Override
    public int nColumns() {
        return 1;
    }
}
