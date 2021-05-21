package com.mygdx.linear;

import com.mygdx.nmethods.CGMFunction;
import com.mygdx.nmethods.Vector;

import java.util.List;

public class CgmSoleFunction implements CGMFunction {
    CSRMatrix a;
    List<Double> b;
    Vector bVec;
    public CgmSoleFunction(CSRMatrix matrix, List<Double> vector) {
        a = matrix;
        b = vector;
        bVec = new Vector(b);
    }

    @Override
    public int getN() {
        return b.size();
    }
    @Override
    public Double apply(Vector arg) {
        return arg.scalarProduct(a.multiply(arg)) - 2 * arg.scalarProduct(b);
    }

    @Override
    public Vector matVecProd(Vector p) {
        return a.multiply(p);
    }

    @Override
    public Vector gradient(Vector x) {
        return a.multiply(x).add(new Vector(b).multiply(-2));
    }



}
