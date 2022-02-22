package io.github.curl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : Pramod Khalkar
 * @since : 22/02/22, Tue
 * description: This file belongs to java-curl-executor
 **/
final class Helper {

    private final static String FORMAT_SPECIFIER = "%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])";

    static int formatSpecifierCount(String str) {
        Pattern pattern = Pattern.compile(FORMAT_SPECIFIER);
        Matcher matcher = pattern.matcher(str);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    static long parseInt(String number) {
        try {
            return Long.parseLong(number);
        } catch (Exception ex) {
            throw new RuntimeException(String.format("%s is not a number", number));
        }
    }
}
