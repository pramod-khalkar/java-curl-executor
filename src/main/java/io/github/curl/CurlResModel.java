package io.github.curl;

import java.io.IOException;
import java.io.InputStream;

/**
 * Date: 05/02/22
 * Time: 4:57 pm
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
class CurlResModel {
    private final int code;
    private final InputStream inputStream;

    CurlResModel(int code, InputStream inputStream) {
        this.code = code;
        this.inputStream = inputStream;
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
        callBack.onResult(new CurlStreamResponse(code, inputStream));
    }

    /**
     * User need to close the stream after read
     */
    CurlStreamResponse streamResponse() {
        return new CurlStreamResponse(code, inputStream);
    }
}
