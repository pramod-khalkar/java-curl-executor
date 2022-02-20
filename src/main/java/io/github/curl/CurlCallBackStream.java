package io.github.curl;

/**
 * Date: 17/02/22
 * Time: 11:33 AM
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
@FunctionalInterface
public interface CurlCallBackStream {
    void onResult(CurlStreamResponse result);
}
