package com.mygdx.linear;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayMatrix implements Matrix {

    private final List<List<MatrixElement>> matrix;

    public ArrayMatrix(int rows, int columns) {
        matrix = Stream.generate(() ->
                Stream.generate(() ->
                        (MatrixElement) new MatrixElementImpl()
                ).limit(columns).collect(Collectors.toList())
        ).limit(rows).collect(Collectors.toList());
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
}
