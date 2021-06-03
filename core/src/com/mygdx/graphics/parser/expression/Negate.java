package com.mygdx.graphics.parser.expression;

public class Negate extends AbstractUnaryOperation {

    public Negate(final Expression target) {
        super(target);
    }

    @Override
    protected String getStringValue() {
        return "-";
    }

    @Override
    protected double apply(final double targetValue) {
        return -targetValue;
    }

    @Override
    public Expression derivative(final String variableName) {
        return new Negate(target.derivative(variableName));
    }

}
