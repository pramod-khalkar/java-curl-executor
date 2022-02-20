package com.github.curl;

import com.github.curl.model.RequestBody;
import io.github.curl.Curl;
import io.github.curl.CurlCallBack;
import io.github.curl.CurlResponse;

/**
 * Date: 17/02/22
 * Time: 2:02 AM
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
public interface RemoteHttpPutCalls {
    @Curl(cmd = "curl -XPUT http://localhost:8080/api/create --basic --user %s:%s -d '%s'")
    void putCallWithAuthShortOptForData(CurlCallBack callBack, String username, String password, RequestBody requestBody);

    @Curl(cmd = "curl -X PUT http://localhost:8080/api/create --basic --user %s:%s --data '%s'")
    void putCallWithAuthLongOptForData(CurlCallBack callBack, String username, String password, RequestBody requestBody);

    @Curl(cmd = "curl -XPUT http://localhost:8080/api/create -H 'Authorization:Bearer %s' --data '%s'")
    void putCallWithBearerAuthInHeader(CurlCallBack callBack, String token, RequestBody requestBody);

    @Curl(cmd = "curl -XPUT --form '@%s' http://localhost:8080/upload -H 'Authorization:Bearer %s'")
    CurlResponse uploadFile(String filePath, String token);

    @Curl(cmd = "curl -XPUT --form 'file_one=@%s' http://localhost:8080/upload --form 'file_two=@%s' -H 'Authorization:Bearer %s'")
    CurlResponse uploadMultipartFiles(String filePathOne, String filePathTwo,String token);

    // TODO: 20/02/22 upload with content and multipart file
    @Curl(cmd = "curl -XPUT --form 'sample=@%s' http://localhost:8080/upload -H 'Authorization:Bearer %s' --data '%s'")
    CurlResponse uploadFileWithBody(String filePath, String token,String requestBody);
}
