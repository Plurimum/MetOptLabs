package com.mygdx.newton;

import com.mygdx.linear.Matrix;
import com.mygdx.nmethods.MatrixImpl;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MatrixSpecific extends MatrixImpl {

    public MatrixSpecific(final Matrix matrix) {
        super(IntStream.range(0, matrix.nRows()).mapToObj(i ->
                IntStream.range(0, matrix.nColumns()).mapToObj(j ->
                        matrix.get(i, j).get()).collect(Collectors.toList())).collect(Collectors.toList()));
    }
}
