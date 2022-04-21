package com.github.sellersj.javahashperf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

/**
 * This is to debug an issue with performance of the NistDataMirror from
 * https://github.com/stevespringett/nist-data-mirror/
 *
 *
 * @author sellersj
 *
 */
public class Application {

    private static final int NUMBER_OF_FILES = 5;

    /** This is for the file that are about 50 mb long. */
    private static final int NUMBER_OF_CHAR_IN_EACH_FILE = 50000000;

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        printSystemInfo();
        createAndCheckFiles(NUMBER_OF_FILES, NUMBER_OF_CHAR_IN_EACH_FILE);
    }

    /**
     * Print some system info to be able to know what is being run.
     */
    public static void printSystemInfo() {
        System.out.println("System info:");

        try {
            System.out.println("Hostname: " + InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // env vars
        List<String> envVarsToCheck = Arrays.asList("NUMBER_OF_PROCESSORS");
        Map<String, String> env = System.getenv();
        for (String envVar : envVarsToCheck) {
            System.out.println(envVar + " : " + env.get(envVar));
        }

        // system vars
        List<String> sysVars = Arrays.asList("java.vendor", "java.vm.name", "java.version");
        Properties properties = System.getProperties();
        for (String sysVar : sysVars) {
            System.out.println(sysVar + " : " + properties.get(sysVar));
        }
    }

    /**
     * Do the work with configurable input.
     *
     * @param numberOfFiles of files to generate
     * @param randomFileSize the size of the files
     */
    public static void createAndCheckFiles(int numberOfFiles, int randomFileSize) {
        List<File> files = createRandomFiles(numberOfFiles, randomFileSize);

        long t1 = System.currentTimeMillis();
        int count = 1;
        for (File file : files) {
            System.out.println(String.format("Checking file %s of %s ", count++, numberOfFiles));
            checkFile(file);
        }
        long t2 = System.currentTimeMillis();
        printTime("It took %s milliseconds to check all of the files", t1, t2);
        System.out.println(String.format("It took %s seconds to check all of the files", (t2 - t1) / 1000));
    }

    /**
     * Create temp files that will be deleted on JVM exit.
     *
     * @param numberOfFiles to create
     * @param fileSize the size of each files in characters
     * @return
     */
    public static List<File> createRandomFiles(int numberOfFiles, int fileSize) {
        List<File> result = new ArrayList<>();
        System.out.println(String.format("About to create %s files", numberOfFiles));
        long t1 = System.currentTimeMillis();

        try {
            for (int i = 0; i < numberOfFiles; i++) {
                File file = File.createTempFile("temp-file-", ".data");
                file.deleteOnExit();

                // fill the file with junk here
                PrintWriter writer = new PrintWriter(file, "UTF-8");
                // write some random data
                writer.println(generateRandomAlphaNumericString(fileSize));
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

    /**
     * @param message to print with a single placeholder
     * @param t1 time 1
     * @param t2 time 2
     */
    public static void printTime(String message, long t1, long t2) {
        System.out.println(String.format(message, (t2 - t1)));
    }

    /**
     * "Check" the file by creating a hash of it.
     *
     * @param file to generate a hash for
     */
    public static void checkFile(File file) {
        long t1 = System.currentTimeMillis();
        String hex = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            hex = checksum(file.getAbsolutePath(), md);

        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException("could not create a check the file", e);
        }

        long t2 = System.currentTimeMillis();
        printTime("It took %s milliseconds to create a checksum which was " + hex, t1, t2);
    }

    /**
     * adapted from https://www.baeldung.com/java-random-string
     *
     * @param targetStringLength string length
     * @return the random string
     */
    public static String generateRandomAlphaNumericString(int targetStringLength) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'

        return RANDOM.ints(leftLimit, rightLimit + 1) //
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)) //
            .limit(targetStringLength) //
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    //////////////////////////////////////////////

    // from NistDataMirror
    private static String checksum(String filepath, MessageDigest md) throws IOException {

        // file hashing with DigestInputStream
        try (DigestInputStream dis = new DigestInputStream(new FileInputStream(filepath), md)) {
            while (dis.read() != -1) {
                ; // empty loop to clear the data
            }
            md = dis.getMessageDigest();
        }

        // bytes to hex
        StringBuilder result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        return result.toString().toUpperCase(Locale.ROOT);

    }

}
