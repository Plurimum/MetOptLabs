package com.mygdx.parser;

public interface ParserAlgebra<T> {

    T multiply(T a, T b);

    T add(T a, T b);

    T negate(T t);

    T divide(T a, T b);

    T subtract(T a, T b);

    T of(double c);

    T of(String variableName);
}
