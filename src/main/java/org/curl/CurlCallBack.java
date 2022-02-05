package org.curl;

/**
 * Date: 05/02/22
 * Time: 10:41 pm
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
public interface CurlCallBack {
    void onSuccess(int responseCode, String successResponse);

    void onError(int responseCode, String errorResponse);
}
