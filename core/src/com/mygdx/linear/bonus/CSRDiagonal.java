package com.mygdx.linear.bonus;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CSRDiagonal extends CSRMatrixImpl {
    public CSRDiagonal(List<Double> vals) {
        super(vals, IntStream.range(0, vals.size()).boxed().collect(Collectors.toList()),
                IntStream.range(0, vals.size() + 1).boxed().collect(Collectors.toList()));
    }

    @Override
    public double get(int i, int j) {
        if (i == j) {
            return vals.get(i);
        } else {
            return 0;
        }
    }
}
