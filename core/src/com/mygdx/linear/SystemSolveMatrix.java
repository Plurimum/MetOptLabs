package com.mygdx.linear;

import java.util.ArrayList;
import java.util.List;

public interface SystemSolveMatrix extends Matrix {
    List<Double> solve(final List<Double> free);

    default List<List<Double>> toList() {
        List<List<Double>> result = new ArrayList<>(nRows());
        for (int i = 0; i < nRows(); ++i) {
            result.add(new ArrayList<>(nColumns()));
            for (int j = 0; j < nColumns(); ++j) {
                result.get(i).add(get(i, j).get());
            }
        }
        return result;
    }
}
