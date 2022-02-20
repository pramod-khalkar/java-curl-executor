package com.github.curl;

import io.github.curl.Curl;
import io.github.curl.CurlCallBack;

/**
 * Date: 16/02/22
 * Time: 5:54 PM
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
public interface RemoteHttpDeleteCalls {
    @Curl(cmd = "curl -XDELETE http://localhost:8080/api/test")
    void simpleDeleteCall_1(CurlCallBack callBack);

    @Curl(cmd = "curl -X DELETE http://localhost:8080/api/test")
    void simpleDeleteCall_2(CurlCallBack callBack);

    @Curl(cmd = "curl -XDELETE http://localhost:8080/api/test/%s")
    void simpleDeleteCall_3(CurlCallBack callBack, String id);
}
