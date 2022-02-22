package io.github.curl;

/**
 * Date: 17/02/22
 * Time: 11:57 AM
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
public class CurlResponse {
    /**
     * HTTP response code
     */
    private final int code;
    /**
     * Response body as a string format
     */
    private final String body;

    CurlResponse(int code, String body) {
        this.code = code;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public String getBody() {
        return body;
    }
}
