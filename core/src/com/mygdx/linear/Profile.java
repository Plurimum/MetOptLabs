package com.mygdx.linear;

import java.util.ArrayList;
import java.util.List;

public class Profile {

    private final List<MatrixElement> values;
    private final List<Integer> separators;

    public Profile(List<MatrixElement> values, List<Integer> separators) {
        this.values = values;
        this.separators = separators;
    }

    public List<MatrixElement> get(int index) {
        return values.subList(separators.get(index != 0 ? index - 1 : 0), separators.get(index));
    }
}
