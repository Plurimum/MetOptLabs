package com.mygdx.linear.bonus;

import com.mygdx.linear.bonus.CSRMatrix;
import com.mygdx.nmethods.Matrix;
import com.mygdx.nmethods.QuadraticFunction;
import com.mygdx.nmethods.Vector;

import java.util.List;

public class CgmSoleFunction extends QuadraticFunction {
    public CgmSoleFunction(com.mygdx.nmethods.Matrix a, List<Double> b) {
        super(a, b, 0);
    }

    @Override
    public int getN() {
        return b.size();
    }

    @Override
    public Double apply(Vector arg) {
        return arg.scalarProduct(a.multiply(arg)) / 2 - 2 * arg.scalarProduct(b);
    }


    @Override
    public Vector gradient(Vector x) {
        return a.multiply(x).add(new Vector(b).multiply(-2));
    }



}
