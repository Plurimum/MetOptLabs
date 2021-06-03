package com.mygdx.graphics.parser.expression;

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
        return Const.ONE;
    }

    @Override
    public String toString() {
        return name;
    }
}
