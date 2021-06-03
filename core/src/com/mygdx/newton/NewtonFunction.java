package com.mygdx.newton;

import com.mygdx.linear.ArrayMatrix;
import com.mygdx.linear.ProfileMatrix;
import com.mygdx.linear.SystemSolveMatrix;
import com.mygdx.nmethods.NFunction;
import com.mygdx.nmethods.Vector;
import com.mygdx.parser.ExpressionAlgebra;
import com.mygdx.parser.ExpressionParser;
import com.mygdx.parser.expression.Expression;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NewtonFunction implements NFunction {

    private final List<Expression> gradient;
    private final List<List<Expression>> hesse;
    private final Expression expression;
    private final List<String> variables;

    public NewtonFunction(final Expression expression) {
        this.expression = expression;
        this.variables = expression.getVariables();
        gradient = variables.stream().map(expression::derivative).collect(Collectors.toList());
        hesse = gradient.stream().map(f -> variables.stream().map(f::derivative).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    public NewtonFunction(final String s) {
        this(new ExpressionParser<>(new ExpressionAlgebra()).parse(s));
    }

    Map<String, Double> getArguments(final List<Double> point) {
        final Map<String, Double> map = new HashMap<>();
        for (int i = 0; i < point.size(); i++) {
            map.put(variables.get(i), point.get(i));
        }
        return map;
    }

    @Override
    public Double apply(final Vector arg) {
        return expression.evaluate(getArguments(arg));
    }

    @Override
    public Vector gradient(final Vector point) {
        final Map<String, Double> args = getArguments(point);
        return gradient.stream().map(f -> f.evaluate(args)).collect(Collectors.toCollection(Vector::new));
    }

    public SystemSolveMatrix hesse(final Vector point) {
        final Map<String, Double> args = getArguments(point);
        final List<List<Double>> s = hesse.stream().map(l -> l.stream().map(f -> f.evaluate(args))
                .collect(Collectors.toList())).collect(Collectors.toList());
        return new ProfileMatrix(new ArrayMatrix(s));
    }

    @Override
    public int getN() {
        return variables.size();
    }
}
