package com.mygdx.graphics.parser;

import com.mygdx.graphics.parser.expression.*;

public class ExpressionAlgebra implements ParserAlgebra<Expression> {
    @Override
    public Expression multiply(final Expression a, final Expression b) {
        return new Multiply(a, b);
    }

    @Override
    public Expression add(final Expression a, final Expression b) {
        return new Add(a, b);
    }

    @Override
    public Expression negate(final Expression expression) {
        return new Negate(expression);
    }

    @Override
    public Expression divide(final Expression a, final Expression b) {
        return new Divide(a, b);
    }

    @Override
    public Expression subtract(final Expression a, final Expression b) {
        return new Subtract(a, b);
    }

    @Override
    public Expression of(final double c) {
        return new Const(c);
    }

    @Override
    public Expression of(final String variableName) {
        return new Variable(variableName);
    }
}
