package com.mygdx.newton.test;

import com.mygdx.graphics.parser.ExpressionParser;
import com.mygdx.linear.ArrayMatrix;
import com.mygdx.newton.ClassicNewtonMethod;
import com.mygdx.newton.SolverQuadraticFunction;
import com.mygdx.nmethods.NMethod;
import com.mygdx.nmethods.NonlinearConjugateGradientMethod;
import com.mygdx.nmethods.QuadraticFunction;
import com.mygdx.nmethods.Vector;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.IntStream;

public class NewtonTest {

    private final Random random = new Random();
    private final int size = 10;
    private final double eps = 1e-4;

    @Before
    void init() {
    }

    @Test
    void classic() {
        parseAndCheck("72*x*x -120*x*y + 72*y*y + 12*x -30*y + 25");
        parseAndCheck("5*x*x + 10*y*y + 24*x + 2");
    }

    ArrayMatrix generateRandomMatrix(final int n) {
        final ArrayMatrix result = new ArrayMatrix(n, n);
        IntStream.range(0, n).forEach(i -> {
            IntStream.range(0, n).forEach(j -> result.get(i, j).set(random.nextInt(n) + 1));
        });
        return result;
    }

    void parseAndCheck(final String s) {
        final QuadraticFunction f = new ExpressionParser().parse(s);
        final NMethod method = new ClassicNewtonMethod<>(new SolverQuadraticFunction(f));
        final Vector res = method.findMin(eps);
        final Vector expected = new NonlinearConjugateGradientMethod<>(f).findMin(eps);
        Assertions.assertEquals(0, res.add(expected.multiply(-1)).length(), 100 * eps);
    }
}
