package com.mygdx.linear;

public class MatrixElementImpl implements MatrixElement {

    private double value;

    private static int counter = 0;
    public MatrixElementImpl() {
        this.value = 0;
    }

    public MatrixElementImpl(double value) {
        this.value = value;
    }

    public static void resetCounter() {
        counter = 0;
    }

    public static void inc() {
        counter++;
    }

    public static int getCounter() {
        return counter;
    }

    @Override
    public double get() {
        inc();
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
