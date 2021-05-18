package com.mygdx.linear;

import java.io.PrintWriter;
import java.util.Arrays;
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
        return Math.max(minSize, random.nextInt(maxSize + 1));
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

    public void generateOne(int maxSize) {
        int size = generateSize(2, maxSize);
        int r = generateCount(size);
        int c = generateCount(size);
        writer.println(size + " " + r + " " + c);
        printElements(size);
        printProfile(size, r);
        printProfile(size, c);
        printElements(size);
    }
}