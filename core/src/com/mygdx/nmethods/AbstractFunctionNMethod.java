package com.mygdx.nmethods;

import java.util.Collections;

public abstract class AbstractFunctionNMethod<F extends NFunction> implements NMethod {

    private final F function;
    private Vector start;

    protected AbstractFunctionNMethod(final F function, final Vector start) {
        this.function = function;
        this.start = start;
    }

    protected AbstractFunctionNMethod(final F function) {
        this(function, new Vector(Collections.nCopies(function.getN(), 0.)));
    }

    public F getFunction() {
        return function;
    }

    public Vector getStart() {
        return start;
    }

    protected void setStart(final Vector start) {
        this.start = start;
    }

}
