package com.github.curl;

import io.github.curl.Curl;
import io.github.curl.CurlCallBack;

/**
 * @author : Pramod Khalkar
 * @since : 22/02/22, Tue
 * description: This file belongs to java-curl-executor
 **/
public interface RemoteHttpDeleteCalls {
    @Curl(cmd = "curl -XDELETE http://localhost:8080/api/test")
    void simpleDeleteCall_1(CurlCallBack callBack);

    @Curl(cmd = "curl -X DELETE http://localhost:8080/api/test")
    void simpleDeleteCall_2(CurlCallBack callBack);

    @Curl(cmd = "curl -XDELETE http://localhost:8080/api/test/%s")
    void simpleDeleteCall_3(CurlCallBack callBack, String id);
}
