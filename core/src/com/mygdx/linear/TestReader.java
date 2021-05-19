package com.mygdx.linear;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestReader {
    private final Scanner scanner;
    private int size;

    public TestReader(Scanner scanner) {
        this.scanner = scanner;
    }

    private static <T> ArrayList<T> readArrayList(Supplier<T> supplier, int size) {
        return Stream
                .generate(supplier)
                .limit(size)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<Integer> readSeparators() {
        return readArrayList(scanner::nextInt, size);
    }

    private ArrayList<MatrixElement> readElements(int size) {
        return readArrayList(() -> new MatrixElementImpl(scanner.nextDouble()), size);
    }

    public ArrayMatrix readArrayMatrix() {
        size = scanner.nextInt();
        List<List<MatrixElement>> m = Stream.generate(() ->
                Stream.generate(() -> (MatrixElement) new MatrixElementImpl(scanner.nextDouble())).limit(size).collect(Collectors.toList()))
                .limit(size).collect(Collectors.toList());
        return new ArrayMatrix(m);
    }

    public ProfileMatrix readProfileMatrix() {
        size = scanner.nextInt();
        int rowsProfileSize = scanner.nextInt(), columnsProfileSize = scanner.nextInt();
        ArrayList<MatrixElement> diag = readArrayList(() -> new MatrixElementImpl(scanner.nextDouble()), size);
        ArrayList<MatrixElement> elements = readElements(rowsProfileSize);
        ArrayList<Integer> separators = readSeparators();
        Profile rows = new Profile(elements, separators);
        elements = readElements(columnsProfileSize);
        separators = readSeparators();
        Profile columns = new Profile(elements, separators);
        return new ProfileMatrix(diag, rows, columns);
    }

    public List<Double> readFreeCoefficients() {
        return readArrayList(scanner::nextDouble, size);
    }
}
