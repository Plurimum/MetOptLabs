package com.mygdx.newton.test;

import com.mygdx.graphics.parser.ExpressionParser;
import com.mygdx.linear.ArrayMatrix;
import com.mygdx.methods.GoldenSectionMethod;
import com.mygdx.newton.ClassicNewtonMethod;
import com.mygdx.newton.OptimizedNewton;
import com.mygdx.newton.SolverQuadraticFunction;
import com.mygdx.nmethods.NMethod;
import com.mygdx.nmethods.NonlinearConjugateGradientMethod;
import com.mygdx.nmethods.QuadraticFunction;
import com.mygdx.nmethods.Vector;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import static java.time.Duration.ofSeconds;

public class NewtonTest {

    private final Random random = new Random();
    private final int size = 10;
    private final double eps = 1e-4;

    @Before
    void init() {
    }

    @Test
    void classic() {
        /*parseAndCheck("72*x*x - 120*x*y + 72*y*y + 12*x -30*y + 25");
        parseAndCheck("5*x*x + 10*y*y + 24*x + 2");
        parseAndCheck("x*x + 2*x*y + 2*y*y + 2*x + 4*y + 3");
        parseAndCheck("18*x*x - 33*x*y + 10*y*y + 2*x + 155*y + 3");
        parseAndCheck("228*x*x - 144*x*y + 101*y*y -30*x + 1*y + 3");*/
        //parseAndCheck("x*x - 1.2*x*y + y*y ");
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
        final Vector expected = new NonlinearConjugateGradientMethod<>(f).findMin(eps);
        final NMethod method = new ClassicNewtonMethod<>(new SolverQuadraticFunction(f), new Vector(Arrays.asList(4., 1.)));
        final AtomicReference<Vector> res = new AtomicReference<>();
        Assertions.assertTimeoutPreemptively(ofSeconds(5), () -> {
            res.set(method.findMin(eps));;
        });
        System.out.println("!" + expected);
        Assertions.assertEquals(0, res.get().add(expected.multiply(-1)).length(), 2 * eps);
    }
}
