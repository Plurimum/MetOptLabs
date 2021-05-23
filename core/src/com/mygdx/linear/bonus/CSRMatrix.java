package com.mygdx.linear.bonus;

import com.mygdx.linear.ArrayMatrix;
import com.mygdx.nmethods.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import com.mygdx.nmethods.Matrix;

public class CSRMatrix extends com.mygdx.nmethods.Matrix {
    List<Double> vals;
    List<Integer> iCols;
    List<Integer> iRows;



    public CSRMatrix(List<Double> vals, List<Integer> iCols, List<Integer> iRows) {
        super(new ArrayList<>());
        this.vals = vals;
        this.iCols = iCols;
        this.iRows = iRows;
    }

    public CSRMatrix(ArrayMatrix m, boolean sparsify) {
        super(new ArrayList<>());
        if (sparsify) {
            m = sparsed(m);
//            System.out.println(m);
        }
        symmetrize(m);
//        System.out.println(m);

        int currIRow = 0;
        iRows = new ArrayList<>();
        iCols = new ArrayList<>();
        vals = new ArrayList<>();
        iRows.add(currIRow);
        for (int i = 0; i < m.nRows(); i++) {
            for (int j = 0; j < m.nColumns(); j++) {
                if (m.get(i, j).get() != 0.0) {
                    vals.add(m.get(i, j).get());
                    currIRow++;
                    iCols.add(j);
                }
            }
            iRows.add(currIRow);
        }
    }

    public CSRMatrix(ArrayMatrix m) {
        this(m, false);
    }

    private static void symmetrize(ArrayMatrix m) {
        for (int i = 0; i < m.nColumns(); i++) {
            for (int j = i + 1; j < m.nColumns(); j++) {
                m.get(j, i).set(m.get(i, j).get());
            }
        }
    }

    private static ArrayMatrix sparsed(ArrayMatrix m) {
        Random random = new Random();
        final ArrayMatrix sparsed = new ArrayMatrix(m.nRows(), m.nColumns());
        IntStream.range(0, m.nRows()).forEach(i -> {
            IntStream.generate(() -> random.nextInt(m.nColumns())).limit(2).distinct().forEach(j ->
                    sparsed.get(i, j).set(m.get(i, j).get()));
        });
        return sparsed;

    }

    @Override
    public double get(int i, int j) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Vector get(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Vector multiply(Vector x) {
        List<Double> v = new ArrayList<>(Collections.nCopies(x.size(), 0.));
        for (int row = 0; row < iRows.size() - 1; row++) {
            for (int i = iRows.get(row); i < iRows.get(row + 1); i++) {
                v.set(row, v.get(row) + vals.get(i) * x.get(iCols.get(i)));
            }
        }
        return new Vector(v);
    }


    @Override
    public int size() {
        return iRows.size() - 1;
    }
}