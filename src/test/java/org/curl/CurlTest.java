package org.curl;

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
        CurlBuilder.build(RemoteCalls.class).call_one(new CurlCallBack() {
            @Override
            public void onSuccess(int responseCode, String successResponse) {
                //Assertions.assertEquals(401, responseCode);
            }

            @Override
            public void onError(int responseCode, String errorResponse) {
                Assertions.assertEquals(401, responseCode);
            }
        });
    }


    public interface RemoteCalls {
        @Curl(cmd = "curl http://localhost:8080/test -H api-key:12345")
        String call_one(CurlCallBack callBack);
    }
}
