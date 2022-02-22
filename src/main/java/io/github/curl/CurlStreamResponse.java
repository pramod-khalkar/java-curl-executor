package io.github.curl;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Date: 17/02/22
 * Time: 12:06 PM
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
public class CurlStreamResponse {
    /**
     * Response as a stream
     */
    private final InputStream in;
    /**
     * HTTP response code
     */
    private final int code;
    /**
     * Header field's from server
     */
    private final Map<String, List<String>> headerFields;

    CurlStreamResponse(int code, InputStream in, Map<String, List<String>> headerFields) {
        this.code = code;
        this.in = in;
        this.headerFields = headerFields;
    }

    public InputStream getInputStream() {
        return in;
    }

    public int getCode() {
        return code;
    }

    public Map<String, List<String>> getHeaderFields() {
        return headerFields;
    }
}
