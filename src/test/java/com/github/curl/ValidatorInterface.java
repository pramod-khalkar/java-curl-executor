package com.github.curl;

import io.github.curl.Curl;
import io.github.curl.CurlCallBack;

/**
 * Date: 17/02/22
 * Time: 2:16 AM
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
public class ValidatorInterface {

    public interface Empty {
    }

    public interface PlaceHolderMissed {
        @Curl(cmd = "curl http://localhost:8080/api/test")
        void methodCall(CurlCallBack callBack, String token);
    }

    public interface ParameterMissed {
        @Curl(cmd = "curl http://localhost:8080/api/test/%s")
        void methodCall(CurlCallBack callBack);
    }

    public interface UnexpectedReturnType {
        @Curl(cmd = "curl http://localhost:8080/api/test")
        Object methodCall(CurlCallBack callBack);
    }

    public interface NeitherReturnOrCallbackAvailable {
        @Curl(cmd = "curl http://localhost:8080/api/test")
        void methodCall();
    }

    public interface CallbackShouldBeFirstMethodParameter {
        @Curl(cmd = "curl http://localhost:8080/api/test?token=%s")
        String methodCall(String token, CurlCallBack callBack);
    }

    public interface CallbackShouldNotBeMultipleMethodsParameters {
        @Curl(cmd = "curl http://localhost:8080/api/test")
        String methodCall(CurlCallBack callBack_1, CurlCallBack callBack_2);
    }
}
