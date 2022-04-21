package com.github.sellersj.javahashperf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ApplicationTest {

    @Test
    public void createRandomFiles() {
        int size = 2;
        List<File> files = Application.createRandomFiles(size);
        assertEquals(2, files.size());

        for (File file : files) {
            assertTrue(file.exists(), file.getAbsolutePath() + " should exist");
            assertTrue(0 != file.length(), "file should have zero lenght but was " + file.length());
        }
    }

    @Test
    public void createAndCheckFiles_SmokeTest() {
        Application.createAndCheckFiles(2);
    }

}
