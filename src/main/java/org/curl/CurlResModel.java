package org.curl;

/**
 * Date: 05/02/22
 * Time: 4:57 pm
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
class CurlResModel {
    private String successResponse;
    private String errorResponse;
    private final int responseCode;

    public CurlResModel(int responseCode, String response, boolean isSuccess) {
        this.responseCode = responseCode;
        if (isSuccess) {
            this.successResponse = response;
        } else {
            this.errorResponse = response;
        }
    }

    public void response(CurlCallBack callback) {
        if (successResponse != null) {
            callback.onSuccess(responseCode, successResponse);
        } else {
            callback.onError(responseCode, errorResponse);
        }
    }

    public String response() {
        return successResponse != null ? successResponse : errorResponse;
    }
}
