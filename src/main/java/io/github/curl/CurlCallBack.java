package io.github.curl;

/**
 * Date: 05/02/22
 * Time: 10:41 pm
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
@FunctionalInterface
public interface CurlCallBack {
    /**
     * @param result : curl command response details
     */
    void onResult(CurlResponse result);
}
