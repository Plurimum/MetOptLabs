package com.mygdx.linear;

import java.util.ArrayList;
import java.util.List;

public class Profile {

    private List<MatrixElement> values;
    private List<Integer> separators;

    public Profile(List<MatrixElement> values, List<Integer> separators) {
        this.values = values;
        this.separators = separators;
    }

    public Profile() {
        values = new ArrayList<>();
        separators = new ArrayList<>();
    }

    @Override
    public String toString() {
        //TODO
        return null;
    }

    public List<MatrixElement> get(int index) {
        return values.subList(separators.get(index != 0 ? index - 1 : 0), separators.get(index));
    }
}
