package com.mygdx.parser.expression;

import java.util.List;
import java.util.Map;

public interface Expression {

    double evaluate(final Map<String, Double> arguments);

    Expression derivative(final String variableName);

    List<String> getVariables();
}
