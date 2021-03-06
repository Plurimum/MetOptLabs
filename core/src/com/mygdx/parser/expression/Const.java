package com.mygdx.parser.expression;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Const implements Expression {

    private final double value;
    public static final Expression ZERO = new Const(0);
    public static final Expression ONE = new Const(1);

    public Const(final double value) {
        this.value = value;
    }

    @Override
    public double evaluate(final Map<String, Double> arguments) {
        return value;
    }

    @Override
    public Expression derivative(final String variableName) {
        return ZERO;
    }

    @Override
    public List<String> getVariables() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public double getValue() {
        return value;
    }
}
