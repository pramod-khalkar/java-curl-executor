package com.github.curl;

import com.github.curl.model.RequestBody;
import io.github.curl.Curl;
import io.github.curl.CurlCallBack;

/**
 * @author : Pramod Khalkar
 * @since : 22/02/22, Tue
 * description: This file belongs to java-curl-executor
 **/
public interface RemoteHttpPostCalls {
    @Curl(cmd = "curl -XPOST http://localhost:8080/api/create --basic --user %s:%s -d '%s'")
    void postCallWithAuthShortOptForData(CurlCallBack callBack, String username, String password, RequestBody requestBody);

    @Curl(cmd = "curl -X POST http://localhost:8080/api/create --basic --user %s:%s --data '%s'")
    void postCallWithAuthLongOptForData(CurlCallBack callBack, String username, String password, RequestBody requestBody);

    @Curl(cmd = "curl -XPOST http://localhost:8080/api/create -H 'Authorization:Bearer %s' --data '%s'")
    void postCallWithBearerAuthInHeader(CurlCallBack callBack, String token, RequestBody requestBody);
}
