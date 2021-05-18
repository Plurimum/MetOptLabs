package com.mygdx.linear;

public class UnmodifiableMatrixElement extends MatrixElementImpl implements MatrixElement {

    UnmodifiableMatrixElement(double value) {
        super(value);
    }

    @Override
    public void set(double value) {
        throw new UnsupportedOperationException("Using set operation for unmodifiable element");
    }
}
