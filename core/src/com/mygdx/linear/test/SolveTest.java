package com.mygdx.linear.test;

import com.mygdx.linear.*;
import com.mygdx.linear.bonus.CSRMatrix;
import com.mygdx.linear.bonus.HashMapMatrix;
import com.mygdx.newton.CholeskyDecomposition;
import com.mygdx.nmethods.Vector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SolveTest {

    private final Random random = new Random();
    private final double eps = 228e-12;

    @Test
    void LU() {
        final ArrayMatrix temp = generateRandomMatrix(4);
        final ProfileMatrix pm = new ProfileMatrix(temp);
        final MatrixPair lu = pm.LU();

        final Matrix result = lu.getL().multiply(lu.getU());

        for (int i = 0; i < pm.nRows(); ++i) {
            for (int j = 0; j < pm.nColumns(); ++j) {
                Assertions.assertEquals(temp.get(i, j).get(), result.get(i, j).get(), eps);
            }
        }
    }

    @Test
    void profileSolve() {
        final int size = 10;
        final ArrayMatrix temp = generateRandomMatrix(size);
        final ProfileMatrix pm = new ProfileMatrix(temp);
        final List<Double> expected = Collections.nCopies(size, 666.);//IntStream.range(0, size).asDoubleStream().boxed().collect(Collectors.toList());
        List<Double> actual = pm.solve(generateFree(temp, expected));
        IntStream.range(0, size).forEach(i -> {
            Assertions.assertEquals(expected.get(i), actual.get(i), eps);
        });
    }

    @Test
    void gaussSolve() {
        final int size = 6;
        final ArrayMatrix temp = generateRandomMatrix(size);
        final List<Double> expected = Collections.nCopies(size, 666.);

        final List<Double> actual = temp.solve(generateFree(temp, expected));
        IntStream.range(0, size).forEach(i -> {
            Assertions.assertEquals(expected.get(i), actual.get(i), eps);
        });
    }

    @Test
    void cholesky() {
        final Matrix a = new ArrayMatrix(Arrays.asList(
                new Vector(Arrays.asList(4., 12., -16.)),
                new Vector(Arrays.asList(12., 37., -43.)),
                new Vector(Arrays.asList(-16., -43., 98.))));
        final CholeskyDecomposition dec = new CholeskyDecomposition(a);
        final Matrix b = dec.getL().multiply(dec.getTransposedL());
        for (int i = 0; i < a.nRows(); i++) {
            for (int j = 0; j < a.nColumns(); j++) {
                assertEquals(a.get(i, j).get(), b.get(i, j).get(), eps);
            }
        }
        final List<Double> expected = Collections.nCopies(a.nRows(), 98174.);
        final List<Double> free = generateFree(dec.getL(), expected);

        final List<Double> actual = dec.getL().solve(free);
        IntStream.range(0, 3).forEach(i -> {
            Assertions.assertEquals(expected.get(i), actual.get(i), eps);
        });

    }

//    @Test
//    void cgmSolve() {
//        final int size = 5;
//        final CSRMatrix  temp = new CSRMatrix(HashMapMatrix.generateSparced(size));
//        final List<Double> expected = IntStream.range(0, size).asDoubleStream().boxed().collect(Collectors.toList());
//        List<Double> actual = temp.solve(temp.multiply(new Vector(expected)));
//        IntStream.range(0, size).forEach(i ->
//                Assertions.assertEquals(expected.get(i), actual.get(i), eps)
//        );
//    }

    ArrayMatrix generateRandomMatrix(final int n) {
        final ArrayMatrix result = new ArrayMatrix(n, n);
        IntStream.range(0, n).forEach(i -> {
            IntStream.range(0, n).forEach(j -> result.get(i, j).set(random.nextInt(n) + 1));
        });
        return result;
    }

    List<Double> generateFree(final Matrix matrix, final List<Double> expected) {
        final Matrix freeMatrix = matrix.multiply(new SingleColumnMatrix(expected));
        return IntStream.range(0, matrix.nRows())
                .mapToObj(i -> freeMatrix.get(i, 0).get())
                .collect(Collectors.toList());
    }


}
