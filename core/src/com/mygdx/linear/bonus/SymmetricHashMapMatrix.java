package com.mygdx.linear.bonus;

import com.mygdx.linear.Matrix;
import com.mygdx.linear.MatrixElement;
import com.mygdx.linear.MatrixElementImpl;
import com.mygdx.linear.UnmodifiableMatrixElement;

import java.util.*;

public class SymmetricHashMapMatrix implements Matrix {

    private final Map<Pair, MatrixElement> values;
    private static final MatrixElement NULL = new UnmodifiableMatrixElement(0);
    private final int nRows;
    private final int nColumns;

    public SymmetricHashMapMatrix(int nRows, int nColumns, List<Double> vals, List<Integer> iRows, List<Integer> iCols) {
        this.nRows = nRows;
        this.nColumns = nColumns;
        this.values = new HashMap<>();
        for (int row = 0; row < nRows; row++) {
            for (int i = iRows.get(row); i < iRows.get(row + 1); i++) {
                Pair pair = Pair.of(row, iCols.get(i));
                values.put(pair, new MatrixElementImpl(vals.get(i)));
                values.put(Pair.reversed(pair), new MatrixElementImpl(vals.get(i)));
            }
        }
    }

    static class Pair {
        public final int r;
        public final int c;

        public Pair(int r, int c) {
            this.r = r;
            this.c = c;
        }

        @Override
        public int hashCode() {
            return r * 10000 + c;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other instanceof Pair) {
                final Pair pair = (Pair) other;
                return r == pair.r && c == pair.c;
            } else {
                return false;
            }
        }

        public static Pair of(int r, int c) {
            return new Pair(r, c);
        }

        public static Pair reversed(final Pair pair) {
            return new Pair(pair.c, pair.r);
        }

    }

    @Override
    public MatrixElement get(int x, int y) {
        return values.getOrDefault(Pair.of(x, y), NULL);
    }

    @Override
    public int nRows() {
        return nRows;
    }

    @Override
    public int nColumns() {
        return nColumns;
    }
}
