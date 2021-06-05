package com.mygdx.newton.test;

import com.mygdx.linear.ArrayMatrix;
import com.mygdx.methods.GoldenSectionMethod;
import com.mygdx.newton.*;
import com.mygdx.nmethods.GradientMethod;
import com.mygdx.nmethods.NMethod;
import com.mygdx.nmethods.Vector;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

public class NewtonTest {

    private final Random random = new Random();
    private final int size = 10;
    private final double eps = 1e-6;
    private final List<String> functions = Arrays.asList(
            "72*x*x - 120*x*y + 72*y*y + 12*x -30*y + 25",
            "5*x*x + 10*y*y + 24*x + 2",
            "x*x + 2*x*y + 2*y*y + 2*x + 4*y + 3",
            "228*x*x - 144*x*y + 101*y*y -30*x + 1*y + 3"
    );
    private final List<ResearchTriple> labFunctionsFirst = Arrays.asList(
            new ResearchTriple(
                    "100*(y - x * x)*(y - x * x) + (1 - x) * (1 - x)",
                    new Vector(Arrays.asList(-1.2, 1.)),
                    new Vector(Arrays.asList(1., 1.))),
            new ResearchTriple(
                    "x * x + y * y - 6 / 5 * x * y",
                    new Vector(Arrays.asList(4., 1.)),
                    new Vector(Arrays.asList(0., 0.)))
    );
    private final List<ResearchTriple> labFunctionsSecond = Arrays.asList(
            new ResearchTriple(
                    "100*(y - x)*(y - x) + (1 - x)*(1 - x)",
                    baseStartPoint(),
                    new Vector(Arrays.asList(1., 1.))),
            new ResearchTriple(
                    "(x * x + y - 11) * (x * x + y - 11) + (x + y * y - 7) * (x + y * y - 7)",
                    baseStartPoint(),
                    new Vector(Arrays.asList(3., 2.))
            )
    );

    private static class ResearchTriple {
        String func;
        Vector start;
        Vector ans;

        ResearchTriple(String func, Vector start, Vector ans) {
            this.func = func;
            this.start = start;
            this.ans = ans;
        }
    }

    @Test
    void classic() {
        functions.forEach(fun -> parseAndCheck(fun, ClassicNewtonMethod::new));
    }

    @Test
    void optimized() {
        functions.forEach(fun -> parseAndCheck(fun, f -> new OptimizedNewton<>(f, GoldenSectionMethod::new)));
    }

    @Test
    void bfsh() {
        functions.forEach(fun -> parseAndCheck(fun, BFShMethod::new));
    }

    @Test
    void powell() {
        functions.forEach(fun -> parseAndCheck(fun, PowellMethod::new));
    }

    @Test
    void marquardtFirst() {
        functions.forEach(fun -> parseAndCheck(fun, MarquardtMethodFirst::new));
    }

    @Test
    void labFunctionsFirst() {
        labFunctionsFirst.forEach(this::checkLabFuncFirst);
    }

    @Test
    void labFunctionsSecond() {
        labFunctionsSecond.forEach(this::checkLabFuncSecond);
    }

    @Test
    void megaFunction() {
        ClassicNewtonMethod<NewtonFunction> classic = new ClassicNewtonMethod<>(
                new NewtonFunction("100*(y - x * x)*(y - x * x) + (1 - x) * (1 - x)"),
                new Vector(Arrays.asList(-1.2, 1.)));
        System.out.println(classic.findMin(eps));
    }

    private void checkLabFuncFirst(ResearchTriple triple) {
        Function<NMethod, Double> f = method -> method.findMin(eps).add(triple.ans.multiply(-1)).length();
        assertEquals(
                0.,
                f.apply(new ClassicNewtonMethod<>(new NewtonFunction(triple.func), triple.start)),
                eps);
        assertEquals(
                0.,
                f.apply(new OptimizedNewton<>(new NewtonFunction(triple.func), triple.start)),
                eps);
    }

    private void checkLabFuncSecond(ResearchTriple triple) {
        Function<NMethod, Double> f = method -> method.findMin(eps).add(triple.ans.multiply(-1)).length();
        assertEquals(
                0.,
                f.apply(new BFShMethod<>(new NewtonFunction(triple.func))),
                eps);
        assertEquals(
                0.,
                f.apply(new PowellMethod<>(new NewtonFunction(triple.func))),
                eps);
    }

    private ArrayMatrix generateRandomMatrix(final int n) {
        final ArrayMatrix result = new ArrayMatrix(n, n);
        IntStream.range(0, n).forEach(i -> {
            IntStream.range(0, n).forEach(j -> result.get(i, j).set(random.nextInt(n) + 1));
        });
        return result;
    }

    private void parseAndCheck(final String s, final Function<NewtonFunction, NMethod> newtonFactory) {
        System.out.println("TESTING " + s);
        final NewtonFunction f = new NewtonFunction(s);
        final NMethod method = newtonFactory.apply(f);
        final Vector expected = new GradientMethod<>(f).findMin(eps);
        final AtomicReference<Vector> res = new AtomicReference<>();
        assertTimeoutPreemptively(ofSeconds(5), () -> {
            res.set(method.findMin(eps));;
        });
        System.out.println("Iterations: " + f.getIterations());
        assertEquals(0, res.get().add(expected.multiply(-1)).length(), 10 * eps);
    }

    private static Vector baseStartPoint() {
        return new Vector(Arrays.asList(0., 0.));
    }
}
