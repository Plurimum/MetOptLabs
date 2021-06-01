package com.mygdx.linear.bonus;

import com.mygdx.nmethods.MatrixImpl;
import com.mygdx.nmethods.QuadraticFunction;

import java.util.List;
import java.util.stream.Collectors;

public class CgmSoleFunction extends QuadraticFunction {
    public CgmSoleFunction(final MatrixImpl a, final List<Double> b) {
        super(a, b.stream().map(i -> -2 * i).collect(Collectors.toList()), 0);
    }


}
