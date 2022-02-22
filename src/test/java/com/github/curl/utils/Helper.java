package com.github.curl.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;

/**
 * Date: 17/02/22
 * Time: 3:38 PM
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
final public class Helper {
    public <T> long calculateTime(Runnable runnable) {
        try {
            long st = System.currentTimeMillis();
            runnable.run();
            long et = System.currentTimeMillis();
            return et - st;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public static String readFromStream(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        return reader.lines().collect(Collectors.joining(""));
    }

    public static String newFileAtResource(String fileNameWithExt) throws IOException {
        File file = new File(String.format("src/test/resources/__files/%s", fileNameWithExt));
        return file.exists() ? file.getAbsolutePath() : file.createNewFile() ? file.getAbsolutePath() : null;
    }

    public static String getFileAbsPath(String fileNameWithExt) {
        return Objects.requireNonNull(Helper.class.getClassLoader().getResource(fileNameWithExt)).getPath();
    }

    public static File convertToFile(InputStream inputStream, String fileNameWithExt) throws IOException {
        File file = new File(String.format("src/test/resources/__files/%s", fileNameWithExt));
        if (!file.exists()) {
            if (file.createNewFile()) {
                try (OutputStream outputStream = new FileOutputStream(file)) {
                    IOUtils.copy(inputStream, outputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }
}
