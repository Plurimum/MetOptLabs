package com.mygdx.linear;


import com.mygdx.linear.bonus.CgmSoleFunction;
import com.mygdx.nmethods.NonlinearConjugateGradientMethod;
import com.mygdx.nmethods.QuadraticFunction;
import com.mygdx.nmethods.Value;
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

public class Test {
    public static void main(String[] args) {
        Path testsDir = Paths.get("Tests");
        try {
            Files.createDirectories(testsDir);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
//        for (int i = 0; i < 10; i++) {
//            String name = "Test" + i;
//            Path dir = testsDir.resolve(name);
//            Path inputFilePath = dir.resolve("input.txt");
//            Path outputFilePath = dir.resolve("output.txt");
//            try {
//                Files.createDirectories(dir);
//            } catch (IOException e) {
//                e.printStackTrace();
//                return;
//            }
//            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(inputFilePath))) {
//                TestGenerator generator = new TestGenerator(writer);
//                generator.generateOne(5);
//            } catch (IOException e) {
//                e.printStackTrace();
//                return;
//            }
//            try (Scanner scanner = new Scanner(Files.newBufferedReader(inputFilePath)).useLocale(Locale.US)) {
//                TestReader testReader = new TestReader(scanner);
//                ProfileMatrix profileMatrix = testReader.readProfileMatrix();
//                try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(outputFilePath))) {
//                    //writer.println(new ArrayMatrix(profileMatrix));
//                    writer.println(ProfileMatrix.solveSystem(profileMatrix, testReader.readFreeCoefficients()));
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                return;
//            }
//        }
//        System.out.println("Testing Gauss");
//        for (int i = 100; i <= 104; i++) {
//            String fileName = "Test" + i + ".txt";
//            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(fileName)))) {
//                TestGenerator generator = new TestGenerator(writer);
//                generator.generateArrayOne(20, true);
//            } catch (IOException e) {
//                e.printStackTrace();
//                return;
//            }
//
//            try (Scanner scanner = new Scanner(Files.newBufferedReader(Paths.get(fileName))).useLocale(Locale.US)) {
//                TestReader testReader = new TestReader(scanner);
//                ArrayMatrix arrayMatrix = testReader.readArrayMatrix();
//                // System.out.println(profileMatrix);
//                System.out.println(ArrayMatrix.solveSystem(arrayMatrix, testReader.readFreeCoefficients()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        System.out.println("Testing CGM");
        for (int i = 104; i <= 107; i++) {
            String fileName = "Test" + i + ".txt";
            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(fileName)))) {
                TestGenerator generator = new TestGenerator(writer);
                generator.generateArrayOne(10, false);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            try (Scanner scanner = new Scanner(Files.newBufferedReader(Paths.get(fileName))).useLocale(Locale.US)) {
                TestReader testReader = new TestReader(scanner);
                com.mygdx.nmethods.Matrix csrMatrix = testReader.readArrayAndSparsify();
                Vector testVec = new Vector(new ArrayList<Double>(Collections.nCopies(csrMatrix.size(), 1.0)));
                QuadraticFunction fun = new CgmSoleFunction(csrMatrix, csrMatrix.multiply(testVec).toList());
                NonlinearConjugateGradientMethod<QuadraticFunction> m = new NonlinearConjugateGradientMethod<>(fun);
                System.out.println(m.findMin(1e-7));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        TestGenerator gen = new TestGenerator(null);
//        for (int n = 10; n <= 1000; n *= 10) {
//            for (int k = 1; k <= 15; ++k) {
//                final ProfileMatrix pm = gen.generateDiagonallyDominant(n, k);
//                final List<Double> correct = IntStream.range(1, n + 1).asDoubleStream().boxed().collect(Collectors.toList());
//                final Matrix temp = pm.multiply(new SingleColumnMatrix(correct));
//                final List<Double> f = IntStream.range(0, n).mapToDouble(i -> temp.get(i, 0).get()).boxed().collect(Collectors.toList());
//                final List<Double> xk = ProfileMatrix.solveSystem(pm, f);
//                Vector v = new Vector(correct);
//                final double diff = v.add(new Vector(xk).multiply(-1)).length();
//                final double diffD = diff / v.length();
//                if (Double.isNaN(diff) || Double.isInfinite(diff)) {
//                    System.out.printf("%d %d No solution No solution%n", n, k);
//                } else {
//                    System.out.printf("%d %d %1.7e %1.7e%n", n, k, diff, diffD);
//                }
//            }
//        }
//        for (int n = 2; n <= 20; n++) {
//            ProfileMatrix pm = TestGenerator.generateHilbert(n);
//            final List<Double> correct = IntStream.range(1, n + 1).asDoubleStream().boxed().collect(Collectors.toList());
//            final Matrix temp = pm.multiply(new SingleColumnMatrix(correct));
//            final List<Double> f = IntStream.range(0, n).mapToDouble(i -> temp.get(i, 0).get()).boxed().collect(Collectors.toList());
//            final List<Double> xk = ProfileMatrix.solveSystem(pm, f);
//            final Vector v = new Vector(correct);
//            final double diff = v.add(new Vector(xk).multiply(-1)).length();
//            final double diffD = diff / v.length();
//            System.out.printf("n=%d, |x*-x_k|=%f, |x*-x_k|/|x*|=%f%n%n", n, diff, diffD);
//        }
    }
}
