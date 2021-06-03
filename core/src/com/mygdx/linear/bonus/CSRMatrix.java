package com.mygdx.linear.bonus;

import com.mygdx.linear.ArrayMatrix;
import com.mygdx.linear.Matrix;
import com.mygdx.nmethods.MatrixImpl;
import com.mygdx.nmethods.NonlinearConjugateGradientMethod;
import com.mygdx.nmethods.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class CSRMatrix extends MatrixImpl {
    protected final List<Double> vals;
    protected final List<Integer> iCols;
    protected final List<Integer> iRows;
    final double EPS = 1e-8;

    public CSRMatrix(List<Double> vals, List<Integer> iCols, List<Integer> iRows) {
        // условия корректной квадратной матрицы:
        // |iRows[i] - iRows[i-1]| <= n \forall i
        // iCols.count(num) <= n \forall num

        // условия нужной матрицы
        // симметрична, положительно определена
        super(new ArrayList<>());
        this.vals = vals;
        this.iCols = iCols;
        this.iRows = iRows;
    }

    public CSRMatrix(Matrix m, boolean modify) {
        super(new ArrayList<>());
        if (modify) {
            m = sparsed(m);
            symmetrize(m);
        }

        int currIRow = 0;
        iRows = new ArrayList<>();
        iCols = new ArrayList<>();
        vals = new ArrayList<>();
        iRows.add(currIRow);
        for (int i = 0; i < m.nRows(); i++) {
            for (int j = 0; j < m.nColumns(); j++) {
                if (Math.abs(m.get(i, j).get()) > EPS) {
                    vals.add(m.get(i, j).get());
                    currIRow++;
                    iCols.add(j);
                }
            }
            iRows.add(currIRow);
        }
    }

    public CSRMatrix(final Matrix m) {
        this(m, false);
    }

    private static void symmetrize(Matrix m) {
        for (int i = 0; i < m.nColumns(); i++) {
            for (int j = i + 1; j < m.nColumns(); j++) {
                m.get(j, i).set(m.get(i, j).get());
            }
        }
    }

    private static ArrayMatrix sparsed(Matrix m) {
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
        for (int row = 0; row < size(); row++) {
            for (int i = iRows.get(row); i < iRows.get(row + 1); i++) {
                v.set(row, v.get(row) + vals.get(i) * x.get(iCols.get(i)));
            }
        }
        return new Vector(v);
    }

    // юзлесс, но вдруг пригодится
    public CSRMatrix transposed() {
        List<Double> tVals = new ArrayList<>(Collections.nCopies(vals.size(), 0.));;
        List<Integer> tIRows = new ArrayList<>(Collections.nCopies(iRows.size(), 0));;
        List<Integer> tICols = new ArrayList<>(Collections.nCopies(iCols.size(), 0));
        for (Integer col : iCols) {
            tIRows.set(col, tIRows.get(col) + 1);
        }
        for (int i = 1; i < tIRows.size(); i++) {
            tIRows.set(i, tIRows.get(i - 1) + tIRows.get(i));
        }

        for (int i = 0; i < size(); i++) {
            for (int j = iRows.get(i); j < iRows.get(i + 1); j++) {
                int newInd = tIRows.get(iCols.get(j) + 1);
                tIRows.set(iCols.get(j) + 1, tIRows.get(iCols.get(j) + 1) + 1);
                tVals.set(newInd, vals.get(j));
                tICols.set(newInd, i);
            }
        }

        return new CSRMatrix(tVals, tICols, tIRows);
    }


    public List<Double> solve(List<Double> free) {
        CgmSoleFunction fun = new CgmSoleFunction(this, free);
        NonlinearConjugateGradientMethod<CgmSoleFunction> m = new NonlinearConjugateGradientMethod<>(fun);
        return m.findMin(EPS);
    }



    @Override
    public int size() {
        return iRows.size() - 1;
    }
}
