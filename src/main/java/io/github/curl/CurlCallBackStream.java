package io.github.curl;

/**
 * @author : Pramod Khalkar
 * @since : 22/02/22, Tue
 * description: This file belongs to java-curl-executor
 **/
@FunctionalInterface
public interface CurlCallBackStream {
    /**
     * @param result : curl command response details with response stream
     */
    void onResult(CurlStreamResponse result);
}
