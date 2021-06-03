package com.mygdx.nmethods;

import com.mygdx.linear.ArrayMatrix;
import com.mygdx.linear.Matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface DoubleMatrix extends List<Vector> {

    double get(final int r, final int c);

    @Override
    Vector get(final int index);

    @Override
    int size();

    default Vector multiply(final Vector other) {
        List<Double> result = new ArrayList<>(Collections.nCopies(size(), 0.));
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                result.set(i, result.get(i) + get(i, j) * other.get(j));
            }
        }
        return new Vector(result);
    }

    default DoubleMatrix add(final DoubleMatrix other) {
        if (other.size() != size()) {
            throw new IllegalArgumentException("Other matrix should have the same size");
        }
        return new MatrixImpl(IntStream.range(0, size())
                .mapToObj(i ->
                        IntStream.range(0, size())
                                .mapToObj(j -> get(i, j) + other.get(i, j))
                                .collect(Collectors.toList()))
                .collect(Collectors.toList()));
    }

    default DoubleMatrix multiply(final double scalar) {
        return new MatrixImpl(IntStream.range(0, size())
                .mapToObj(i ->
                        IntStream.range(0, size())
                                .mapToObj(j -> get(i, j) * scalar)
                                .collect(Collectors.toList()))
                .collect(Collectors.toList()));
    }

    default DoubleMatrix multiply(final DoubleMatrix other) {
        if (size() != other.size()) {
            throw new IllegalArgumentException("Other matrix should have the same size");
        }
        List<List<Double>> result = IntStream.range(0, size())
                .mapToObj(i -> IntStream.range(0, size())
                        .mapToObj(j -> 0.)
                        .collect(Collectors.toCollection(ArrayList::new)))
                .collect(Collectors.toCollection(ArrayList::new));
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                for (int k = 0; k < size(); k++) {
                    result.get(i).set(j, result.get(i).get(j) + get(i, k) * other.get(k, j));
                }
            }
        }
        return new MatrixImpl(result);
    }

    default DoubleMatrix transposed() {
        List<List<Double>> result = IntStream.range(0, size())
                .mapToObj(i -> IntStream.range(0, size())
                        .mapToObj(j -> 0.)
                        .collect(Collectors.toCollection(ArrayList::new)))
                .collect(Collectors.toCollection(ArrayList::new));
        for (int i = 0; i < size(); i++) {
            for (int j = i + 1; j < size(); j++) {
                double tmp = get(i, j);
                result.get(i).set(j, get(j, i));
                result.get(j).set(i, tmp);
            }
        }
        return new MatrixImpl(result);
    }
 }
