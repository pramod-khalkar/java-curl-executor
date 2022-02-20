package com.github.curl.execution;

import static com.github.curl.utils.TestConstant.DELAY_5000_MILLIS;
import static com.github.curl.utils.TestConstant.REQUEST_BODY;
import static com.github.curl.utils.TestConstant.RESPONSE_BODY;
import static com.github.curl.utils.TestConstant.TEST_USERNAME;
import static com.github.curl.utils.TestConstant.TEST_USER_PASSWORD;
import static com.github.tomakehurst.wiremock.client.WireMock.aMultipart;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.curl.RemoteHttpPutCalls;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.github.curl.CurlBuilder;
import io.github.curl.CurlResponse;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Date: 17/02/22
 * Time: 2:07 AM
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
@WireMockTest(httpPort = 8080)
public class PutCmdExeTest {
    private final RemoteHttpPutCalls remoteHttpPutCalls = CurlBuilder.build(RemoteHttpPutCalls.class);
    CallBackAssertion assertSuccess = new CallBackAssertion(200, RESPONSE_BODY.toString(), null, true);

    @Test
    public void simple_put_call() throws InterruptedException {
        stubFor(put("/api/create")
                .withBasicAuth(TEST_USERNAME, TEST_USER_PASSWORD)
                .withRequestBody(equalTo(REQUEST_BODY.toString()))
                .willReturn(ok()
                        .withFixedDelay(DELAY_5000_MILLIS)
                        .withHeader("Content-Type", "application/json")
                        .withBody(RESPONSE_BODY.toString())));

        final CountDownLatch lock = new CountDownLatch(2);
        assertSuccess.setLock(lock);
        remoteHttpPutCalls.putCallWithAuthLongOptForData(assertSuccess, TEST_USERNAME, TEST_USER_PASSWORD, REQUEST_BODY);
        remoteHttpPutCalls.putCallWithAuthShortOptForData(assertSuccess, TEST_USERNAME, TEST_USER_PASSWORD, REQUEST_BODY);
        assertTrue(lock.await(DELAY_5000_MILLIS + 1000, MILLISECONDS));
    }

    @Test
    public void put_call_with_bearer_token() throws InterruptedException {
        stubFor(put("/api/create")
                .withHeader("Authorization", equalTo("Bearer dummy_token"))
                .withRequestBody(equalTo(REQUEST_BODY.toString()))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(RESPONSE_BODY.toString())));
        final CountDownLatch lock = new CountDownLatch(1);
        assertSuccess.setLock(lock);
        remoteHttpPutCalls.putCallWithBearerAuthInHeader(assertSuccess, "dummy_token", REQUEST_BODY);
        assertTrue(lock.await(DELAY_5000_MILLIS + 1000, MILLISECONDS));
    }

    /**
     * https://github.com/wiremock/wiremock/blob/master/src/test/java/com/github/tomakehurst/wiremock/MultipartBodyMatchingAcceptanceTest.java
     */
    @Test
    public void uploading_file() {
        stubFor(put("/upload")
                .withHeader("Authorization", equalTo("Bearer dummy_token"))
                .withMultipartRequestBody(aMultipart().withName("file").withBody(containing("\"name\": \"abc\"")))
                .willReturn(ok(RESPONSE_BODY.editMessage("file received").toString())));

        String filePath = this.getClass().getClassLoader().getResource("sample_data_file_1.txt").getPath();
        CurlResponse response = remoteHttpPutCalls.uploadFile(filePath, "dummy_token");
        assertEquals(200, response.getCode());
        assertEquals(RESPONSE_BODY.editMessage("file received").toString(), response.getBody());
    }

    @Test
    public void uploading_multiple_files() {
        stubFor(put("/upload")
                .withHeader("Authorization", equalTo("Bearer dummy_token"))
                .withMultipartRequestBody(aMultipart().withName("file_one").withBody(containing("\"name\": \"abc\"")))
                .withMultipartRequestBody(aMultipart().withName("file_two").withBody(containing("\"city\": \"pune\"")))
                .willReturn(ok(RESPONSE_BODY.editMessage("files received").toString())));

        String filePathOne = this.getClass().getClassLoader().getResource("sample_data_file_1.txt").getPath();
        String filePathTwo = this.getClass().getClassLoader().getResource("sample_data_file_2.txt").getPath();
        CurlResponse response = remoteHttpPutCalls.uploadMultipartFiles(filePathOne, filePathTwo, "dummy_token");
        assertEquals(200, response.getCode());
        assertEquals(RESPONSE_BODY.editMessage("files received").toString(), response.getBody());
    }

    @Disabled
    @Test
    public void upload_file_with_request_body() {
        stubFor(put("/upload")
                .withHeader("Authorization", equalTo("Bearer dummy_token"))
                .withRequestBody(equalTo(REQUEST_BODY.toString()))
                .withMultipartRequestBody(aMultipart().withName("sample").withBody(containing("\"name\": \"abc\"")))
                .willReturn(ok(RESPONSE_BODY.editMessage("file received").toString())));

        String filePath = this.getClass().getClassLoader().getResource("sample_data_file_1.txt").getPath();
        CurlResponse response = remoteHttpPutCalls.uploadFileWithBody(filePath, "dummy_token",REQUEST_BODY.toString());
        assertEquals(200, response.getCode());
        assertEquals(RESPONSE_BODY.editMessage("file received").toString(), response.getBody());
    }
}
