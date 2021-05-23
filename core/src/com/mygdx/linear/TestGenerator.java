package com.mygdx.linear;

import com.mygdx.linear.bonus.CSRMatrix;

import java.io.PrintWriter;
import java.util.Random;
import java.util.stream.IntStream;

public class TestGenerator {

    private final PrintWriter writer;
    private final Random random;

    TestGenerator(PrintWriter writer) {
        this.writer = writer;
        this.random = new Random();
    }

    private MatrixElement generateElement() {
        return new MatrixElementImpl(random.nextDouble() * 10);
    }

    private int generateSize(int minSize, int maxSize) {
        return randomRange(minSize, maxSize + 1);
    }

    private int randomRange(int left, int right) {
        return left + random.nextInt(right - left);
    }

    private int generateCount(int size) {
        return random.nextInt(Math.min((size - 1) * size / 2, 100));
    }

    private void printElements(int count) {
        IntStream.range(0, count).forEach(i -> writer.print(generateElement().toString() + " "));
        writer.println();
    }

    private void printProfile(int size, int r) {
        printElements(r);
        int[] seps = new int[size];
        IntStream.range(0, r).forEach(i -> {
            while (true) {
                int number = generateSize(1, size - 1);
                if (seps[number] < number) {
                    seps[number]++;
                    break;
                }
            }
        });
        IntStream.range(1, seps.length).forEach(i -> seps[i] += seps[i - 1]);
        IntStream.range(0, seps.length).forEach(i -> writer.print(seps[i] + " "));
        writer.println();
    }

    public void generateOne(int size) {
        int r = generateCount(size);
        int c = generateCount(size);
        writer.println(size + " " + r + " " + c);
        printElements(size);
        printProfile(size, r);
        printProfile(size, c);
        printElements(size);
    }

    public void generateArrayOne(int size, boolean withVec) {
        int s = generateSize(2, size);
        writer.println(s);
        IntStream.range(0, s).forEach(i -> printElements(s));
        if (withVec) {
            printElements(s);
        }
    }


    public ProfileMatrix generateDiagonallyDominant(final int n, final int k) {
        final Matrix matrix = new ArrayMatrix(n, n);
        final int L = -4;
        final int R = 1;
        IntStream.range(0, n).forEach(i -> {
            double sum = 0;
            for (int j = 0; j < i; j++) {
                final int a = randomRange(L, R);
                final int b = randomRange(L, R);
                matrix.get(i, j).set(a);
                matrix.get(j, i).set(b);
                sum += a + b;
            }
            matrix.get(i, i).set(-sum + (i == 0 ? Math.pow(10, -k) : 0));
        });
        return new ProfileMatrix(matrix);
    }

    public static ProfileMatrix generateHilbert(int n) {
        final Matrix matrix = new ArrayMatrix(n, n);
        IntStream.range(0, n).forEach(i -> {
            IntStream.range(0, n).forEach(j -> {
                matrix.get(i, j).set(1. / ((i + 1) + (j + 1) - 1));
            });
        });
        return new ProfileMatrix(matrix);
    }

    public CSRMatrix generateDiagonallyDominantCSR(final int n) {
        final ArrayMatrix matrix = new ArrayMatrix(n, n);
        final int L = -4;
        final int R = 1;
        IntStream.range(1, n).forEach(i -> {
            int randomIndex = randomRange(0, i);
            double sum = randomRange(L, R);
            matrix.get(i, randomIndex).set(sum);

            for (int j = 0; j < i; j++) {
                if (j != randomIndex && random.nextDouble() > (n - 10.) / n) {
                    final int a = randomRange(L, R);
                    final int b = randomRange(L, R);
                    matrix.get(i, j).set(a);
                    matrix.get(j, i).set(b);
                    sum += a + b;
                }
            }
            matrix.get(i, i).set(-sum);
        });
        matrix.get(0, 0).set(1);
        return new CSRMatrix(matrix, false);
    }


}