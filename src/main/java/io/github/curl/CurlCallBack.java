package io.github.curl;

/**
 * @author : Pramod Khalkar
 * @since : 22/02/22, Tue
 * description: This file belongs to java-curl-executor
 **/
@FunctionalInterface
public interface CurlCallBack {
    /**
     * @param result : curl command response details
     */
    void onResult(CurlResponse result);
}
