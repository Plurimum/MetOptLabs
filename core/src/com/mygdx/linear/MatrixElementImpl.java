package com.mygdx.linear;

public class MatrixElementImpl implements MatrixElement {

    private double value;

    public MatrixElementImpl() {
        this.value = 0;
    }

    public MatrixElementImpl(double value) {
        this.value = value;
    }

    @Override
    public double get() {
        return value;
    }

    @Override
    public void set(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
