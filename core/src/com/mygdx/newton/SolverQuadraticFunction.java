package com.mygdx.newton;

import com.mygdx.linear.ArrayMatrix;
import com.mygdx.linear.ProfileMatrix;
import com.mygdx.linear.SystemSolveMatrix;
import com.mygdx.nmethods.QuadraticFunction;

import java.util.List;

public class SolverQuadraticFunction extends QuadraticFunction {

    private final SystemSolveMatrix solver;

    public SolverQuadraticFunction(final SystemSolveMatrix a, final List<Double> b, final double c) {
        super(new MatrixSpecific(a), b, c);
        this.solver = a;
    }

    public SolverQuadraticFunction(final QuadraticFunction other) {
        this(new ProfileMatrix(new ArrayMatrix(other.getA())), other.getB(), other.getC());
    }

    public SystemSolveMatrix getSolver() {
        return solver;
    }
}
