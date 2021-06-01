package com.mygdx.linear;

import java.util.List;

public interface SystemSolveMatrix extends Matrix {
    List<Double> solve(final List<Double> free);
}
