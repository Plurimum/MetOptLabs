package com.mygdx.graphics.parser.expression;

import com.mygdx.graphics.parser.ExpressionAlgebra;
import com.mygdx.graphics.parser.ExpressionParser;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpressionTest {
    final ExpressionParser<Expression> parser = new ExpressionParser<>(new ExpressionAlgebra());

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
    void parser() {
        final Expression expr = parser.parse("1 + (d + 1 / 10) + 2 * (-1 + 3)");
        System.out.println(expr);
    }
}
