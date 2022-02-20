package io.github.curl;

import java.io.InputStream;

/**
 * Date: 17/02/22
 * Time: 12:06 PM
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
public class CurlStreamResponse {
    private final InputStream in;
    private final int code;

    CurlStreamResponse(int code, InputStream in) {
        this.code = code;
        this.in = in;
    }

    public InputStream getInputStream() {
        return in;
    }

    public int getCode() {
        return code;
    }
}
