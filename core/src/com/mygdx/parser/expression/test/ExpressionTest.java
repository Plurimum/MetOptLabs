package com.mygdx.parser.expression.test;

import com.mygdx.parser.ExpressionAlgebra;
import com.mygdx.parser.ExpressionParser;
import com.mygdx.parser.expression.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpressionTest {
    final ExpressionParser<Expression> parser = new ExpressionParser<>(new ExpressionAlgebra());
    private static final double EPS = 1e-5;
    private final Map<String, Double> variables = new HashMap<String, Double>(){{
        put("x", 1.);
        put("y", 2.);
        put("z", 3.);
    }};

    @Test
    void sumZeros() {
        final Expression expr = new Add(Const.ZERO, Const.ZERO);
        assertEquals(0, expr.evaluate(Collections.emptyMap()));
        assertEquals(0, expr.derivative("x").evaluate(Collections.emptyMap()));
    }

    @Test
    void easy() {
        final Expression expr = new Multiply(new Const(4), new Const(5));
        assertEquals(20, expr.evaluate(Collections.emptyMap()));
        assertEquals(0, expr.derivative("x").evaluate(Collections.emptyMap()));
    }

    @Test
    void baseParse() {
        final Expression expr = parser.parse("1 + (d + 1 / 10) + 2 * (-1 + 3)");
        assertEquals(15.1, expr.evaluate(Collections.singletonMap("d", 10.)));
    }

    @Test
    void allOpsParse() {
        Map<String, Double> variable = Collections.singletonMap("x", 10.);
        assertEquals(0.1, parser.parse("1 / x").evaluate(variable), EPS);
        assertEquals(11., parser.parse("1 + x").evaluate(variable), EPS);
        assertEquals(-9., parser.parse("1 - x").evaluate(variable), EPS);
        assertEquals(10., parser.parse("1 * x").evaluate(variable), EPS);
        assertEquals(-10., parser.parse("-x").evaluate(variable), EPS);
        assertEquals(5., parser.parse("5").evaluate(variable), EPS);
        assertEquals(10., parser.parse("x").evaluate(variable), EPS);
        assertEquals(100., parser.parse("x^2").evaluate(variable), EPS);
        assertEquals(1000., parser.parse("x^3").evaluate(variable), EPS);
        assertEquals(20.,
                parser.parse("      ((       \n\n\n\n\n\n  ((((10 +     x  ))))       )) ").evaluate(variable), EPS);
    }

    @Test
    void derivative() {
        Map<String, Double> variable = Collections.singletonMap("x", 10.);
        assertEquals(1., parser.parse("x").derivative("x").evaluate(variable), EPS);
        assertEquals(20., parser.parse("x * x").derivative("x").evaluate(variable), EPS);
        assertEquals(6., parser.parse("x * y * z").derivative("x").evaluate(variables), EPS);
        assertEquals(24., parser.parse("2 * x * y * y * z").derivative("y").evaluate(variables), EPS);
        assertEquals(6. + 12., parser.parse("2*x^3 + 3*x*y^2\n\n     ").derivative("x").evaluate(variables), EPS);
    }

    @Test
    void string() {
        final List<Double> xs = Arrays.asList(1., 2., 3., 4., 5., 6.);
        final Expression expr = parser.parse("72*x^2 - 120*x*y + 72*y^2 + 12*x -30*y + 25").derivative("x");
        final Expression expr1 = parser.parse("72*x*x - 120*x*y + 72*y*y + 12*x -30*y + 25").derivative("x");
        for (final double x : xs) {
            Map<String, Double> map = new HashMap<String, Double>(){{put("x", x); put("y", 1000.);}};
            assertEquals(expr.evaluate(map), expr1.evaluate(map), 1e-8);
        }
    }

    @Test
    void opsPriorities() {
        final Map<String, Double> variable = Collections.singletonMap("x", 2.);
        assertEquals(6., parser.parse("2 + 2 * 2").evaluate(variable), EPS);
        assertEquals(8., parser.parse("(2 + 2) * 2").evaluate(variable), EPS);
        assertEquals(-2., parser.parse("2 - 2 * 2").evaluate(variable), EPS);
        assertEquals(0., parser.parse("(2 - 2) * 2").evaluate(variable), EPS);
        assertEquals(3., parser.parse("2 + 2 / 2").evaluate(variable), EPS);
        assertEquals(2., parser.parse("(2 + 2) / 2").evaluate(variable), EPS);
        assertEquals(2., parser.parse("(2 + 2) * 2 / (2 + 2 - (2 - 2))").evaluate(variable), EPS);
        assertEquals(2., parser.parse("(x + 2) * x / (x + x - (x - 2))").evaluate(variable), EPS);
        assertEquals(4., parser.parse("((2 + 2)) - 0 / (--2) * 555").evaluate(variable), EPS);
        assertEquals(6., parser.parse("x--y--z").evaluate(variables), EPS);

        assertEquals(6., parser.parse("2 + 2 ^ 2").evaluate(variable), EPS);
        assertEquals(16., parser.parse("(2 ^ 2) ^ 2").evaluate(variable), EPS);
        assertEquals(8., parser.parse("2 ^ 2 + 2 ^ 2").evaluate(variable), EPS);
        assertEquals(16., parser.parse("2 ^ 2 * 2 ^ 2").evaluate(variable), EPS);
    }

    @Test
    void evaluateEasy() {
        final double result = new Subtract(
                new Multiply(
                        new Const(2),
                        new Variable("x")
                ),
                new Const(3)
        ).evaluate(Collections.singletonMap("x", 5.));
        assertEquals(7., result, EPS);
    }

    @Test
    void representationEquivalence() {
        final Expression explicitExpr = new Add(
                new Multiply(
                        new Const(2),
                        new Variable("x")
                ),
                new Negate(new Variable("y"))
        );
        final Expression parsedExpr = parser.parse("2 * x + (-y)");
        final Map<String, Double> variables = new HashMap<>();
        final Random random = new Random();
        variables.put("x", random.nextDouble());
        variables.put("y", random.nextDouble());
        assertEquals(explicitExpr.evaluate(variables), parsedExpr.evaluate(variables), EPS);
    }

    @Test
    void powConst() {
        final Expression explicitExpr = new PowConst(
                new Multiply(
                        new Add(new Const(2), new Variable("z2")),
                        new Variable("x1")
                ),
                3);

        final Expression parsedExpr = parser.parse("((2 + z2)*x1)^3 ");
        final Map<String, Double> variables = new HashMap<>();
        final Random random = new Random();
        variables.put("x1", random.nextDouble());
        variables.put("z2", random.nextDouble());
        assertEquals(explicitExpr.evaluate(variables), parsedExpr.evaluate(variables), EPS);
    }
}
