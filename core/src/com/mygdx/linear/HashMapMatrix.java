package com.mygdx.linear;

import java.util.HashMap;
import java.util.Random;
import java.util.stream.IntStream;

public class HashMapMatrix extends HashMap<Integer, MatrixElement>  implements Matrix {
    private int size;
    private static final MatrixElement ZERO = new UnmodifiableMatrixElement(0);
    private boolean symmetrized = false;
    private boolean transposed = false;
    public HashMapMatrix(int size) {
        this.size = size;
    }

    @Override
    public int nRows() {
        return size;
    }

    @Override
    public int nColumns() {
        return size;
    }

    @Override
    public MatrixElement get(int i, int j) {
        int a = i;
        int b = j;
        if (transposed) {
            a = j;
            b = i;
        }
        if (symmetrized) {
            a = Math.min(i, j);
            b = Math.max(i, j);
        }
        return getOrDefault(toKey(a, b), ZERO);
    }

    private Integer toKey(int a, int b) {
        return a * size + b;
    }

    public void set(int i, int j, MatrixElement val) {
        super.put(i * size + j, val);
    }

    public static HashMapMatrix generateSparced(int size) {
        HashMapMatrix m = new HashMapMatrix(size);
        Random rand = new Random();
        IntStream.range(0, size).forEach(i -> {
            int inRow = rand.nextInt(3) + 1;
            for (int t = 0; t < inRow; t++) {
                MatrixElement el = new MatrixElementImpl(rand.nextDouble() * 20);
                m.set(i, rand.nextInt(size), el);
            }
        });
        return m;
    }

    public void transpose() {
        transposed = !transposed;
    }

    public void symmetrize() {
        symmetrized = true;
    }


}
