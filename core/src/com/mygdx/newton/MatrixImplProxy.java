package com.mygdx.newton;

import com.mygdx.linear.Matrix;
import com.mygdx.nmethods.MatrixImpl;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MatrixImplProxy extends MatrixImpl {

    public MatrixImplProxy(final Matrix matrix) {
        super(IntStream.range(0, matrix.nRows()).mapToObj(i ->
                IntStream.range(0, matrix.nColumns()).mapToObj(j ->
                        matrix.get(i, j).get()).collect(Collectors.toList())).collect(Collectors.toList()));
    }
}
