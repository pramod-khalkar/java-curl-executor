package com.github.curl.validation;

import io.github.curl.Curl;
import io.github.curl.CurlResponse;

/**
 * Date: 17/02/22
 * Time: 5:49 PM
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
interface NonPublicInterface {
    @Curl(cmd = "curl http://localhost:8080/api/test")
    CurlResponse methodCall();
}
