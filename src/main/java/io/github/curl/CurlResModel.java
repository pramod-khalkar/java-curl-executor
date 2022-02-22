package io.github.curl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author : Pramod Khalkar
 * @since : 22/02/22, Tue
 * description: This file belongs to java-curl-executor
 **/
class CurlResModel {
    private final int code;
    private final InputStream inputStream;
    private final Map<String, List<String>> headerFields;

    CurlResModel(int code, InputStream inputStream, Map<String, List<String>> headerFields) {
        this.code = code;
        this.inputStream = inputStream;
        this.headerFields = headerFields;
    }

    void response(CurlCallBack callback) {
        String response;
        try {
            response = CurlExecution.readFromStream(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        callback.onResult(new CurlResponse(code, response));
    }

    CurlResponse response() throws IOException {
        return new CurlResponse(code, CurlExecution.readFromStream(inputStream));
    }

    /**
     * User need to close the stream after read
     */
    void streamResponse(CurlCallBackStream callBack) {
        callBack.onResult(new CurlStreamResponse(code, inputStream, this.headerFields));
    }

    /**
     * User need to close the stream after read
     */
    CurlStreamResponse streamResponse() {
        return new CurlStreamResponse(code, inputStream, this.headerFields);
    }
}
