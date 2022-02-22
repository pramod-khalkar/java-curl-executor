package com.github.curl.validation;

import io.github.curl.Curl;
import io.github.curl.CurlResponse;

/**
 * @author : Pramod Khalkar
 * @since : 22/02/22, Tue
 * description: This file belongs to java-curl-executor
 **/
interface NonPublicInterface {
    @Curl(cmd = "curl http://localhost:8080/api/test")
    CurlResponse methodCall();
}
