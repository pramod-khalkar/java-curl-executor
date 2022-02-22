package com.github.curl.utils;

import com.github.curl.model.RequestBody;
import com.github.curl.model.ResponseBody;

/**
 * @author : Pramod Khalkar
 * @since : 22/02/22, Tue
 * description: This file belongs to java-curl-executor
 **/
final public class TestConstant {
    public static final int DELAY_5_SECONDS = 5;
    public static final int DELAY_1000_MILLIS = 1000;
    public static final int DELAY_5000_MILLIS = DELAY_1000_MILLIS * DELAY_5_SECONDS;
    public static final String TEST_USERNAME = "dummy_user";
    public static final String TEST_USER_PASSWORD = "1234";
    public static final String SAMPLE_JSON_RESPONSE = "{\"name\":\"abc\",\"exp\":20}";
    public static final RequestBody REQUEST_BODY = new RequestBody("abc", "p@ssword");
    public static final ResponseBody RESPONSE_BODY = new ResponseBody("Successfully added");
}
