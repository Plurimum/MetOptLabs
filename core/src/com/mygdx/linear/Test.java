package com.mygdx.linear;


import com.mygdx.linear.bonus.CSRDiagonal;
import com.mygdx.linear.bonus.CSRMatrix;
import com.mygdx.linear.bonus.CgmSoleFunction;
import com.mygdx.nmethods.*;
import com.mygdx.nmethods.Vector;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) {
/*
        Path testsDir = Paths.get("Tests");
        try {
            Files.createDirectories(testsDir);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        for (int i = 0; i < 10; i++) {
            String name = "Test" + i;
            Path dir = testsDir.resolve(name);
            Path inputFilePath = dir.resolve("input.txt");
            Path outputFilePath = dir.resolve("output.txt");
            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(inputFilePath))) {
                TestGenerator generator = new TestGenerator(writer);
                generator.generateOne(5);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            try (Scanner scanner = new Scanner(Files.newBufferedReader(inputFilePath)).useLocale(Locale.US)) {
                TestReader testReader = new TestReader(scanner);
                ProfileMatrix profileMatrix = testReader.readProfileMatrix();
                try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(outputFilePath))) {
                    //writer.println(new ArrayMatrix(profileMatrix));
                    writer.println(ProfileMatrix.solveSystem(profileMatrix, testReader.readFreeCoefficients()));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }*/

        System.out.println("Testing Gauss");
        for (int i = 2; i <= 20; i++) {
            ArrayMatrix arrayMatrix = new ArrayMatrix(TestGenerator.generateHilbertArray(i));
            final List<Double> correct = IntStream.range(1, i + 1).asDoubleStream().boxed().collect(Collectors.toList());
            final Matrix temp = arrayMatrix.multiply(new SingleColumnMatrix(correct));
            final List<Double> f = IntStream.range(0, i).mapToDouble(j -> temp.get(j, 0).get()).boxed().collect(Collectors.toList());
            final List<Double> xk = ArrayMatrix.solveSystem(arrayMatrix, f);
            final Vector v = new Vector(correct);
            final double diff = v.add(new Vector(xk).multiply(-1)).length();
            final double diffD = diff / v.length();
            System.out.printf("%d %1.7e %1.7e %n", i, diff, diffD);
        }


        /*System.out.println("Testing CGM");
        Random random = new Random();
        for (int i = 0; i < 1; i++) {
            System.out.printf("%1.7e %1.7e %1.7e %n", random.nextDouble() * 10e-00, random.nextDouble() * 10e-04, random.nextDouble() * 10);
        }*/
        /*final TestGenerator generator = new TestGenerator(null);
        for (int n = 10; n <= 100000; n *= 10) {
            for (int k = 1; k <= 3; ++k) {
                CSRMatrix matrix = generator.generateDiagonallyDominantCSR(n);
                Vector correctX = new Vector(IntStream.range(1, 1 + matrix.size()).asDoubleStream().boxed().collect(Collectors.toList()));
                Vector f = matrix.multiply(correctX);
                CgmSoleFunction fun = new CgmSoleFunction(matrix, matrix.multiply(correctX).toList());
                NonlinearConjugateGradientMethod<CgmSoleFunction> m = new NonlinearConjugateGradientMethod<>(fun);
                final Vector resultX = m.findMin(1e-9).stream().map(x -> x / 2).collect(Collectors.toCollection(Vector::new));
                // System.out.println(testVec);
                // System.out.println(result);
                final Vector ax = matrix.multiply(resultX);
                final double diff = correctX.add(resultX.multiply(-1)).length();
                final double diffD = diff / correctX.length();
                final double cond = diffD / ((f.add(ax.multiply(-1))).length() / f.length()) ;
                if (resultX.stream().anyMatch(x -> x == 0.) || Double.isNaN(diff) || Double.isInfinite(diff)) {
                    System.out.printf("%d %d No_solution No_solution No_solution%n", n, k);
                } else {
                    System.out.printf("%d %d %1.7e %1.7e %1.7e%n", n, k, diff, diffD, cond);
                }
            }
        }*/

        /*
        Random gen = new Random();
        for (int i = 0; i < 10; ++i) {
            List<Double> diag = Stream.generate(gen::nextDouble).limit(8).collect(Collectors.toList());
            com.mygdx.nmethods.Matrix csrMatrix = new CSRDiagonal(diag);
            Vector testVec =
                    new Vector(IntStream.range(0, csrMatrix.size()).mapToObj(Double::valueOf).collect(Collectors.toList()));
            CgmSoleFunction fun = new CgmSoleFunction(csrMatrix, csrMatrix.multiply(testVec).toList());
            NonlinearConjugateGradientMethod<CgmSoleFunction> m = new NonlinearConjugateGradientMethod<>(fun);
            final Vector result = m.findMin(1e-7).stream().map(x -> x / 2).collect(Collectors.toCollection(Vector::new));
            System.out.println(result);
        }
        */

/*
        TestGenerator gen = new TestGenerator(null);
        for (int n = 10; n <= 1000; n *= 10) {
            for (int k = 1; k <= 15; ++k) {
                final ProfileMatrix pm = gen.generateDiagonallyDominant(n, k);
                final List<Double> correct = IntStream.range(1, n + 1).asDoubleStream().boxed().collect(Collectors.toList());
                final Matrix temp = pm.multiply(new SingleColumnMatrix(correct));
                final List<Double> f = IntStream.range(0, n).mapToDouble(i -> temp.get(i, 0).get()).boxed().collect(Collectors.toList());
                final List<Double> xk = ProfileMatrix.solveSystem(pm, f);
                Vector v = new Vector(correct);
                final double diff = v.add(new Vector(xk).multiply(-1)).length();
                final double diffD = diff / v.length();
                if (Double.isNaN(diff) || Double.isInfinite(diff)) {
                    System.out.printf("%d %d No solution No solution%n", n, k);
                } else {
                    System.out.printf("%d %d %1.7e %1.7e%n", n, k, diff, diffD);
                }
            }
        }
        for (int n = 2; n <= 20; n++) {
            ProfileMatrix pm = TestGenerator.generateHilbert(n);
            final List<Double> correct = IntStream.range(1, n + 1).asDoubleStream().boxed().collect(Collectors.toList());
            final Matrix temp = pm.multiply(new SingleColumnMatrix(correct));
            final List<Double> f = IntStream.range(0, n).mapToDouble(i -> temp.get(i, 0).get()).boxed().collect(Collectors.toList());
            final List<Double> xk = ProfileMatrix.solveSystem(pm, f);
            final Vector v = new Vector(correct);
            final double diff = v.add(new Vector(xk).multiply(-1)).length();
            final double diffD = diff / v.length();
            System.out.printf("n=%d, |x*-x_k|=%f, |x*-x_k|/|x*|=%f%n%n", n, diff, diffD);
        }
 */

       /* TestGenerator gen = new TestGenerator(null);
        for (int n = 10; n <= 1000; n *= 10) {
            for (int k = 1; k <= 15; ++k) {
                final ArrayMatrix pm = new ArrayMatrix(gen.generateDiagonallyDominant(n, k));
                final List<Double> correct = IntStream.range(1, n + 1).asDoubleStream().boxed().collect(Collectors.toList());
                final Matrix temp = pm.multiply(new SingleColumnMatrix(correct));
                final List<Double> f = IntStream.range(0, n).mapToDouble(i -> temp.get(i, 0).get()).boxed().collect(Collectors.toCollection(ArrayList::new));
                final List<Double> xk = ArrayMatrix.solveSystem(pm, f);
                Vector v = new Vector(correct);
                final double diff = v.add(new Vector(xk).multiply(-1)).length();
                final double diffD = diff / v.length();
                if (Double.isNaN(diff) || Double.isInfinite(diff)) {
                    System.out.printf("%d %d No_solution No_solution%n", n, k);
                } else {
                    System.out.printf("%d %d %1.7e %1.7e%n", n, k, diff, diffD);
                }
            }
        }*/


    }


}
