package com.mygdx.linear;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ArrayMatrix implements Matrix {

    private final List<List<MatrixElementImpl>> matrix;

    public ArrayMatrix(int rows, int columns) {
        matrix = Stream.generate(() ->
                Stream.generate(MatrixElementImpl::new
                ).limit(columns).collect(Collectors.toList())
        ).limit(rows).collect(Collectors.toList());
    }

    public ArrayMatrix(Matrix matrix) {
        this(matrix.nRows(), matrix.nColumns());
        IntStream.range(0, matrix.nRows())
                .forEach(i -> IntStream.range(0, matrix.nColumns())
                                .forEach(j -> this.get(i, j).set(matrix.get(i, j).get())));
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

    @Override
    public String toString() {
        return matrix.stream()
                .map(list ->
                        list.stream()
                                .map(MatrixElementImpl::toString)
                                .collect(Collectors.joining(" ")))
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
