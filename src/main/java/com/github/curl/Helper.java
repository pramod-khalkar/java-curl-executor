package com.github.curl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 05/02/22
 * Time: 6:39 pm
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
final class Helper {

    private final static String FORMAT_SPECIFIER = "%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])";

    public static int formatSpecifierCount(String str) {
        Pattern pattern = Pattern.compile(FORMAT_SPECIFIER);
        Matcher matcher = pattern.matcher(str);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}
