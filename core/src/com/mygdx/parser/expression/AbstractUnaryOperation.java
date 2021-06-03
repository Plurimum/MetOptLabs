package com.mygdx.parser.expression;

import java.util.List;
import java.util.Map;

public abstract class AbstractUnaryOperation implements Expression {

    final protected Expression target;

    protected AbstractUnaryOperation(final Expression target) {
        this.target = target;
    }

    @Override
    public double evaluate(final Map<String, Double> arguments) {
        return apply(target.evaluate(arguments));
    }

    protected abstract String getStringValue();

    protected abstract double apply(double targetValue);

    @Override
    public List<String> getVariables() {
        return target.getVariables();
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getStringValue(), target);
    }
}
