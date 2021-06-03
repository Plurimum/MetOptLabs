package com.mygdx.parser.expression;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Variable implements Expression {

    final String name;

    public Variable(final String name) {
        this.name = name;
    }

    @Override
    public double evaluate(final Map<String, Double> arguments) {
        return arguments.get(name);
    }

    @Override
    public Expression derivative(final String variableName) {
        return variableName.equals(name) ? Const.ONE : Const.ZERO;
    }

    @Override
    public List<String> getVariables() {
        return Collections.singletonList(name);
    }

    @Override
    public String toString() {
        return name;
    }


}
