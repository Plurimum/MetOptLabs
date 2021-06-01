package com.mygdx.linear;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProfileMatrix implements SystemSolveMatrix {

    private final ArrayList<MatrixElement> diag;
    private final Profile rows;
    private final Profile columns;
    private MatrixPair decomposedLU = null;

    public ProfileMatrix(ArrayList<MatrixElement> diag, Profile rows, Profile columns) {
        this.diag = diag;
        this.rows = rows;
        this.columns = columns;
    }

    public ProfileMatrix(Matrix matrix) {
        if (matrix.nColumns() != matrix.nRows()) {
            throw new IllegalArgumentException("cerf");
        }
        diag = new ArrayList<>(matrix.nRows());
        IntStream.range(0, matrix.nRows()).forEach(i -> diag.add(new MatrixElementImpl(matrix.get(i, i).get())));
        List<MatrixElement> rValues = new ArrayList<>();
        List<MatrixElement> cValues = new ArrayList<>();
        List<Integer> rSeparators = IntStream.range(0, matrix.nRows()).boxed().collect(Collectors.toCollection(ArrayList::new));
        for (int i = 0; i < matrix.nRows(); i++) {
            for (int j = 0; j < i; j++) {
                rValues.add(new MatrixElementImpl(matrix.get(i, j).get()));
                cValues.add(new MatrixElementImpl(matrix.get(j, i).get()));
            }
            if (i > 0) {
                rSeparators.set(i, rSeparators.get(i) + rSeparators.get(i - 1));
            }
        }
        rows = new Profile(rValues, rSeparators);
        columns = new Profile(cValues, Collections.unmodifiableList(rSeparators));
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

    private int start(final int index, final Profile p) {
        return index - p.get(index).size();
    }

    private int maxStart(int i, int j) {
        return Math.max(start(i, rows), start(j, columns));
    }

    public MatrixPair LU() {
        if (decomposedLU == null)  {
            for (int i = 0; i < size(); i++) {
                for (int j = start(i, rows); j < i; j++) {
                    MatrixElement lowerIJ = get(i, j);
                    for (int k = maxStart(i, j); k < j; k++) {
                        lowerIJ.set(lowerIJ.get() - get(i, k).get() * get(k, j).get());
                    }
                }
                for (int j = start(i, columns); j < i; j++) {
                    MatrixElement uji = get(j, i);
                    for (int k = maxStart(j, i); k < j; k++) {
                        uji.set(uji.get() - get(j, k).get() * get(k, i).get());
                    }
                    uji.set(uji.get() / get(j, j).get());
                }
                for (int k = maxStart(i, i); k < i; k++) {
                    get(i, i).set(get(i, i).get() - get(i, k).get() * get(k, i).get());
                }
            }
            decomposedLU = new MatrixPair(this);
        }
        return decomposedLU;
    }

    @Override
    public int nRows() {
        return size();
    }

    @Override
    public int nColumns() {
        return size();
    }

    @Override
    public List<Double> solve(final List<Double> free) {
        final MatrixPair decomposed = LU();
        final Matrix l = decomposed.getL();
        final List<Double> result = new ArrayList<>(free);
        IntStream.range(0, l.nRows()).forEach(i -> {
            IntStream.range(i - rows.get(i).size(), i).forEach(j ->
                    result.set(i, result.get(i) - result.get(j) * get(i, j).get()));
            result.set(i, result.get(i) / l.get(i, i).get());
        });
        for (int j = nColumns() - 1; j >= 0; j--) {
            for (int i = j - columns.get(j).size(); i < j; i++) {
                result.set(i, result.get(i) - result.get(j) * get(i, j).get());
            }
        }
        return result;
    }
}
