package com.mygdx.linear.bonus;

import com.mygdx.linear.bonus.CSRMatrix;
import com.mygdx.nmethods.Matrix;
import com.mygdx.nmethods.QuadraticFunction;
import com.mygdx.nmethods.Vector;

import java.util.List;
import java.util.stream.Collectors;

public class CgmSoleFunction extends QuadraticFunction {
    public CgmSoleFunction(final com.mygdx.nmethods.Matrix a, final List<Double> b) {
        super(a, b.stream().map(i -> -2 * i).collect(Collectors.toList()), 0);
    }


}
