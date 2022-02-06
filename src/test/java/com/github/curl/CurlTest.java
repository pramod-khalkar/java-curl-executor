package com.github.curl;

import io.github.jcurl.Curl;
import io.github.jcurl.CurlBuilder;
import io.github.jcurl.CurlCallBack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Date: 06/02/22
 * Time: 12:31 am
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
public class CurlTest {

    @Test
    public void failedTest() {
        RemoteCalls remoteCalls = CurlBuilder.build(RemoteCalls.class);
        remoteCalls.call_one(new CurlCallBack() {
            @Override
            public void onSuccess(int responseCode, String successResponse) {
            }

            @Override
            public void onError(int responseCode, String errorResponse) {
                Assertions.assertEquals(502, responseCode);
            }
        });
    }

    public interface RemoteCalls {
        @Curl(cmd = "curl http://localhost:8080/test -H api-key:12345")
        String call_one(CurlCallBack callBack);
    }
}
