package com.mygdx.parser.expression;

import java.util.*;

public abstract class AbstractBinaryExpression implements Expression {

    protected final Expression lhs;
    protected final Expression rhs;

    protected AbstractBinaryExpression(final Expression lhs, final Expression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }
    @Override
    public double evaluate(final Map<String, Double> arguments) {
        return apply(lhs.evaluate(arguments), rhs.evaluate(arguments));
    }

    @Override
    public Expression derivative(final String variableName) {
        return applyDerivative(lhs.derivative(variableName), rhs.derivative(variableName));
    }

    @Override
    public List<String> getVariables() {
        final Set<String> result = new HashSet<>(lhs.getVariables());
        result.addAll(rhs.getVariables());
        return new ArrayList<>(result);
    }

    protected abstract String stringValue();

    protected abstract double apply(final double lhs, final double rhs);

    protected abstract Expression applyDerivative(final Expression lhsD, final Expression rhsD);

    @Override
    public String toString() {
        return String.format("(%s %s %s)", lhs, stringValue(), rhs);
    }
}
