package com.mygdx.graphics;

import com.mygdx.newton.NewtonFunction;
import com.mygdx.nmethods.Value;
import com.mygdx.nmethods.Vector;
import com.mygdx.parser.expression.Expression;

import java.util.ArrayList;
import java.util.List;

public class RenderFunction extends NewtonFunction {
    public final List<Value<Vector, Double>> renderPoints = new ArrayList<>();

    public RenderFunction(final Expression expression) {
        super(expression);
    }

    @Override
    public Double apply(final Vector arg) {
        Value<Vector, Double> value = new Value<>(arg, super::apply);
        return value.getFValue();
    }

}
