package com.mygdx.newton.test;

import com.mygdx.graphics.parser.ParserFunction;
import com.mygdx.linear.ArrayMatrix;
import com.mygdx.linear.ProfileMatrix;
import com.mygdx.newton.ClassicNewtonMethod;
import com.mygdx.newton.SolverQuadraticFunction;
import com.mygdx.nmethods.NMethod;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NewtonTest {

    private SolverQuadraticFunction qf;
    private final Random random = new Random();
    private final int size = 10;

    @Before
    void init() {
        qf = new SolverQuadraticFunction(new ProfileMatrix(generateRandomMatrix(size)),
                IntStream.range(0, size).asDoubleStream().boxed().collect(Collectors.toList()), 0);
    }

    @Test
    void classic() {
        final NMethod method = new ClassicNewtonMethod<>(new SolverQuadraticFunction(new ParserFunction(2, "x*x + ")));
        System.out.println(method.findMin(1e-3));
    }

    ArrayMatrix generateRandomMatrix(final int n) {
        final ArrayMatrix result = new ArrayMatrix(n, n);
        IntStream.range(0, n).forEach(i -> {
            IntStream.range(0, n).forEach(j -> result.get(i, j).set(random.nextInt(n) + 1));
        });
        return result;
    }
}
