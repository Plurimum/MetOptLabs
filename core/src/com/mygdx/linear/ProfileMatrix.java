package com.mygdx.linear;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ProfileMatrix implements Matrix {

    private final ArrayList<MatrixElement> diag;
    private final Profile rows;
    private final Profile columns;

    public ProfileMatrix(ArrayList<MatrixElement> diag, Profile rows, Profile columns) {
        this.diag = diag;
        this.rows = rows;
        this.columns = columns;
    }

    public int size() {
        return diag.size();
    }

    @Override
    public MatrixElement get(int row, int column) {
        if (row == column) {
            return diag.get(row);
        } else if (column < row) {
            int size = rows.get(row).size();
            if (column < row - size) {
                return new UnmodifiableMatrixElement(0);
            } else {
                return rows.get(row).get(column - (row - size));
            }
        } else {
            int size = columns.get(column).size();
            if (row < column - size) {
                return new UnmodifiableMatrixElement(0);
            } else {
                return columns.get(column).get(row - (column - size));
            }
        }
    }

    private static int start(int index, Profile p) {
        return index - p.get(index).size();
    }

    private static int maxStart(int i, int j, ProfileMatrix matrix) {
        return Math.max(start(i, matrix.rows), start(j, matrix.columns));
    }

    public static MatrixPair LU(ProfileMatrix matrix) {
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = start(i, matrix.rows); j < i; j++) {
                MatrixElement lowerIJ = matrix.get(i, j);
                for (int k = maxStart(i, j, matrix); k < j; k++) {
                    lowerIJ.set(lowerIJ.get() - matrix.get(i, k).get() * matrix.get(k, j).get());
                }
            }
            for (int j = start(i, matrix.columns); j < i; j++) {
                MatrixElement uji = matrix.get(j, i);
                for (int k = maxStart(j, i, matrix); k < j; k++) {
                    uji.set(uji.get() - matrix.get(j, k).get() * matrix.get(k, i).get());
                }
                uji.set(uji.get() / matrix.get(j, j).get());
            }
            for (int k = maxStart(i, i, matrix); k < i; k++) {
                matrix.get(i, i).set(matrix.get(i, i).get() - matrix.get(i, k).get() * matrix.get(k, i).get());
            }
        }
        return new MatrixPair(matrix);
    }

    public static List<Double> solveSystem(ProfileMatrix a, List<Double> b) {
        final MatrixPair decomposed = LU(a);
        final Matrix l = decomposed.getL();
        final List<Double> result = new ArrayList<>(b);
        IntStream.range(0, l.nRows()).forEach(i -> {
            IntStream.range(i - a.rows.get(i).size(), i).forEach(j -> {
                result.set(i, result.get(i) - result.get(j) * a.get(i, j).get());
            });
            result.set(i, result.get(i) / l.get(i, i).get());
        });
        for (int j = a.nColumns() - 1; j >= 0; j--) {
            for (int i = j - a.columns.get(j).size(); i < j; i++) {
                result.set(i, result.get(i) - result.get(j) * a.get(i, j).get());
            }
        }
        return result;
    }

    @Override
    public int nRows() {
        return size();
    }

    @Override
    public int nColumns() {
        return size();
    }
}
