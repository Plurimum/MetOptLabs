package com.mygdx.parser;

import com.mygdx.parser.ParserAlgebra;
import com.mygdx.parser.ParserFunction;

public class QuadraticAlgebra implements ParserAlgebra<ParserFunction> {
    @Override
    public ParserFunction multiply(final ParserFunction a, final ParserFunction b) {
        return a.multiply(b);
    }

    @Override
    public ParserFunction add(final ParserFunction a, final ParserFunction b) {
        return a.add(b);
    }

    @Override
    public ParserFunction negate(final ParserFunction parserFunction) {
        return parserFunction.negative();
    }

    @Override
    public ParserFunction divide(final ParserFunction a, final ParserFunction b) {
        throw new UnsupportedOperationException("divide");
    }

    @Override
    public ParserFunction subtract(final ParserFunction a, final ParserFunction b) {
        return a.add(b.negative());
    }

    @Override
    public ParserFunction of(final double c) {
        return new ParserFunction(2, c);
    }

    @Override
    public ParserFunction of(final String variableName) {
        return new ParserFunction(2, variableName);
    }
}
