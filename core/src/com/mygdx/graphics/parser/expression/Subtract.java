package com.mygdx.graphics.parser.expression;

public class Subtract extends AbstractBinaryExpression {

    public Subtract(final Expression lhs, final Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    protected String stringValue() {
        return "-";
    }

    @Override
    protected double apply(final double lhs, final double rhs) {
        return lhs - rhs;
    }

    @Override
    protected Expression applyDerivative(final Expression lhsD, final Expression rhsD) {
        return new Subtract(lhsD, rhsD);
    }
}
