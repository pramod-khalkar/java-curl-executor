package com.github.curl.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

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
}
