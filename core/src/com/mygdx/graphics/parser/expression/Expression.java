package com.mygdx.graphics.parser.expression;

import java.util.Map;

public interface Expression {

    double evaluate(final Map<String, Double> arguments);

    Expression derivative(final String variableName);
}
