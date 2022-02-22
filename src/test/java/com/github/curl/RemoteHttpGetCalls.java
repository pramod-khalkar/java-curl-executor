package com.github.curl;

import io.github.curl.Curl;
import io.github.curl.CurlCallBack;
import io.github.curl.CurlCallBackStream;
import io.github.curl.CurlResponse;
import io.github.curl.CurlStreamResponse;

/**
 * Date: 15/02/22
 * Time: 12:02 am
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
public interface RemoteHttpGetCalls {
    /**
     * Response as stream
     */
    @Curl(cmd = "curl http://localhost:8080/api/test")
    CurlStreamResponse simpleGetCallWithStreamOfResponse();

    /**
     * Response as stream
     */
    @Curl(cmd = "curl http://localhost:8080/api/test")
    void simpleGetCallWithStreamOfResponse(CurlCallBackStream callBack);

    @Curl(cmd = "curl http://localhost:8080/api/test")
    void simpleGetCall_1(CurlCallBack callBack);

    @Curl(cmd = "curl -XGET http://localhost:8080/api/test")
    CurlResponse simpleGetCall_2();

    @Curl(cmd = "curl -X GET http://localhost:8080/api/test")
    void simpleGetCall_3(CurlCallBack callBack);

    @Curl(cmd = "curl -X GET http://localhost:8080/api/test -H api-key:1234")
    void getCallWithHeader(CurlCallBack callBack);

    @Curl(cmd = "curl -X GET http://localhost:8080/api/test -H api-key:wrong_key")
    void getCallWithIncorrectHeaderKey(CurlCallBack callBack);

    /**
     * Authorization: Basic <base64_credential> <- internal curl conversion
     *
     * @param callBack
     * @return
     */
    @Curl(cmd = "curl -X GET http://localhost:8080/api/test  -u dummy_user:1234")
    void getCallWithBasicAuthShortOption(CurlCallBack callBack);

    @Curl(cmd = "curl -X GET http://localhost:8080/api/test  --user dummy_user:1234")
    void getCallWithBasicAuthLongOption(CurlCallBack callBack);

    @Curl(cmd = "curl -X GET http://localhost:8080/api/test  --basic --user dummy_user:1234")
    void getCallWithBasicAuthFlagSet_1(CurlCallBack callBack);

    @Curl(cmd = "curl -X GET http://localhost:8080/api/test  --basic -u dummy_user:1234")
    void getCallWithBasicAuthFlagSet_2(CurlCallBack callBack);

    @Curl(cmd = "curl -X GET http://localhost:8080/api/test  -H 'Authorization:Basic %s'")
    void getCallWithBasicAuthDirectInHeader(CurlCallBack callBack, String base64EncodedHeader);

    @Curl(cmd = "curl -X GET http://localhost:8080/api/test  -H'Authorization:Basic %s'")
    void getCallWithBasicAuthDirectInHeaderNoSpace(CurlCallBack callBack, String base64EncodedHeader);

    /**
     * Authorization: Bearer <token>
     *
     * @param callBack
     * @param token
     */
    @Curl(cmd = "curl -X GET http://localhost:8080/api/test  -H \"Authorization: Bearer %s\" -H \"Accept: application/json\"")
    void getCallWithBearerAuthDirectInHeader(CurlCallBack callBack, String token);

    /**
     * Sync and Async test
     */
    @Curl(cmd = "curl http://localhost:8080/api/test")
    CurlResponse syncCall();

    @Curl(cmd = "curl http://localhost:8080/api/test")
    void AsyncCall(CurlCallBack callBack);

    /**
     * connection timeout test (time will be in seconds)
     */
    @Curl(cmd = "curl http://localhost:8080/api/test --connect-timeout 2")
    CurlResponse connectionTimeOutGetCall();

    /**
     * read timeout test (time will be in seconds)
     *
     * @return
     */
    @Curl(cmd = "curl http://localhost:8080/api/test --max-time %d")
    CurlResponse readTimeOutGetCall(Integer timeout);

    /**
     * upload download
     */
    @Curl(cmd = "curl http://localhost:8080/download")
    CurlStreamResponse txtFileDownload();

    @Curl(cmd = "curl http://localhost:8080/download")
    CurlStreamResponse imgFileDownload();

    @Curl(cmd = "curl http://localhost:8080/download")
    CurlStreamResponse pdfFileDownload();
}
