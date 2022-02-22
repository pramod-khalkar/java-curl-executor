package io.github.curl;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author : Pramod Khalkar
 * @since : 22/02/22, Tue
 * description: This file belongs to java-curl-executor
 **/
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
