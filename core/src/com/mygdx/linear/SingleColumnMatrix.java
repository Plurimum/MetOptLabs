package com.mygdx.linear;

import java.util.List;
import java.util.stream.Collectors;

public class SingleColumnMatrix implements Matrix {

    private final List<MatrixElement> list;

    public SingleColumnMatrix(final List<Double> list) {
        this.list = list.stream().map(MatrixElementImpl::new).collect(Collectors.toList());
    }

    @Override
    public MatrixElement get(int x, int y) {
        if (y >= 1) {
            throw new IndexOutOfBoundsException("Single column is 1-dimension");
        }
        return list.get(x);
    }

    @Override
    public int nRows() {
        return list.size();
    }

    @Override
    public int nColumns() {
        return 1;
    }
}
