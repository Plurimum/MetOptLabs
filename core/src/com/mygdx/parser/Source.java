package com.mygdx.parser;

public interface Source {
    boolean hasNext();

    char next();

    int getPos();
}
