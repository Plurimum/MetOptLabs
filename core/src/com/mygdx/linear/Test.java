package com.mygdx.linear;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws IOException {
        for (int i = 0; i <= 4; i++) {
            String fileName = "Test" + i + ".txt";
            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(fileName)))) {
                TestGenerator generator = new TestGenerator(writer);
                generator.generateOne(20);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            try (Scanner scanner = new Scanner(Files.newBufferedReader(Paths.get(fileName))).useLocale(Locale.US)) {
                TestReader testReader = new TestReader(scanner);
                ProfileMatrix profileMatrix = testReader.readProfileMatrix();
                // System.out.println(profileMatrix);
                System.out.println(ProfileMatrix.solveSystem(profileMatrix, testReader.readFreeCoefficients()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
