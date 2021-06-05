package com.mygdx.parser.expression;

import java.util.List;
import java.util.Map;

public class PowConst implements Expression {
    private final Expression expression;
    private final int exp;

    public PowConst(final Expression expression, final int exp) {
        this.expression = expression;
        this.exp = exp;
    }

    @Override
    public double evaluate(final Map<String, Double> arguments) {
        return Math.pow(expression.evaluate(arguments), exp);
    }

    @Override
    public Expression derivative(final String variableName) {
        return new Multiply(expression.derivative(variableName), new Multiply(new Const(exp), new PowConst(expression, exp - 1)));
    }

    @Override
    public List<String> getVariables() {
        return expression.getVariables();
    }

    @Override
    public String toString() {
        return String.format("(%s) ^ %d", expression, exp);
    }
}
