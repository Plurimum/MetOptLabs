package com.mygdx.parser.expression;

public class Multiply extends AbstractBinaryExpression {

    public Multiply(final Expression lhs, final Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    protected String stringValue() {
        return "*";
    }

    @Override
    protected double apply(final double lhs, final double rhs) {
        return lhs * rhs;
    }

    @Override
    protected Expression applyDerivative(final Expression lhsD, final Expression rhsD) {
        return new Add(
                new Multiply(lhsD, rhs),
                new Multiply(lhs, rhsD));
    }
}
