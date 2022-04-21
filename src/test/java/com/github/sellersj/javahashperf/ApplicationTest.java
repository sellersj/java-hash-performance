package com.github.sellersj.javahashperf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ApplicationTest {

    private static final int TEST_FILE_SIZE = 1024;

    @Test
    public void printSystemInfo_SmokeTest() {
        Application.printSystemInfo();
    }

    @Test
    public void createRandomFiles() {
        int size = 2;
        List<File> files = Application.createRandomFiles(size, TEST_FILE_SIZE);
        assertEquals(2, files.size());

        for (File file : files) {
            assertTrue(file.exists(), file.getAbsolutePath() + " should exist");
            assertTrue(0 != file.length(), "file should have zero lenght but was " + file.length());
        }
    }

    @Test
    public void generateRandomAlphaNumericString() {
        int size = 1024;
        String randomString = Application.generateRandomAlphaNumericString(size);
        assertEquals(size, randomString.length());
    }

    @Test
    public void createAndCheckFiles_SmokeTest() {
        Application.createAndCheckFiles(2, TEST_FILE_SIZE);
    }

}
