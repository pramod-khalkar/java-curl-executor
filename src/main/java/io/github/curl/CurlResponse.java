package io.github.curl;

/**
 * @author : Pramod Khalkar
 * @since : 22/02/22, Tue
 * description: This file belongs to java-curl-executor
 **/
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
