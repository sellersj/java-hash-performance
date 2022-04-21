package com.github.sellersj.javahashperf;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Application {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        System.out.println("Hello world");
    }

    public static List<File> createRandomFiles(int size) {
        List<File> result = new ArrayList<>();
        System.out.println(String.format("About to create %s files", size));
        long t1 = System.currentTimeMillis();

        try {
            for (int i = 0; i < size; i++) {
                File file = File.createTempFile("temp-file-", ".data");
                file.deleteOnExit();

                // fill the file with junk here
                PrintWriter writer = new PrintWriter(file, "UTF-8");
                // TODO make this random data
                writer.println("The first line");
                writer.println("The second line");
                writer.close();

                result.add(file);
            }
        } catch (IOException e) {
            throw new RuntimeException("could not create a random file", e);
        }
        long t2 = System.currentTimeMillis();
        printTime("It took %s milliseconds to create the random files", t1, t2);

        return result;
    }

    public static void printTime(String message, long t1, long t2) {
        System.out.println(String.format(message, (t2 - t1)));
    }

}
