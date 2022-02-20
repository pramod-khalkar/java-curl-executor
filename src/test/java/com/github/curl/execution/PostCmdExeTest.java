package com.github.curl.execution;

import static com.github.curl.utils.TestConstant.DELAY_1000_MILLIS;
import static com.github.curl.utils.TestConstant.REQUEST_BODY;
import static com.github.curl.utils.TestConstant.RESPONSE_BODY;
import static com.github.curl.utils.TestConstant.TEST_USERNAME;
import static com.github.curl.utils.TestConstant.TEST_USER_PASSWORD;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.curl.RemoteHttpPostCalls;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.github.curl.CurlBuilder;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.Test;

/**
 * Date: 16/02/22
 * Time: 6:13 PM
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
@WireMockTest(httpPort = 8080)
public class PostCmdExeTest {
    private final RemoteHttpPostCalls remoteHttpPostCalls = CurlBuilder.build(RemoteHttpPostCalls.class);
    CallBackAssertion assertSuccess = new CallBackAssertion(200, RESPONSE_BODY.toString(), null, true);

    @Test
    public void simple_post_call() throws InterruptedException {
        stubFor(post("/api/create")
                .withBasicAuth(TEST_USERNAME, TEST_USER_PASSWORD)
                .withRequestBody(equalTo(REQUEST_BODY.toString()))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(RESPONSE_BODY.toString())));
        CountDownLatch lock = new CountDownLatch(2);
        assertSuccess.setLock(lock);
        remoteHttpPostCalls.postCallWithAuthShortOptForData(assertSuccess, TEST_USERNAME, TEST_USER_PASSWORD, REQUEST_BODY);
        remoteHttpPostCalls.postCallWithAuthLongOptForData(assertSuccess, TEST_USERNAME, TEST_USER_PASSWORD, REQUEST_BODY);
        assertTrue(lock.await(DELAY_1000_MILLIS, MILLISECONDS));
    }

    @Test
    public void post_call_with_bearer_token() throws InterruptedException {
        stubFor(post("/api/create")
                .withHeader("Authorization", equalTo("Bearer dummy_token"))
                .withRequestBody(equalTo(REQUEST_BODY.toString()))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(RESPONSE_BODY.toString())));
        CountDownLatch lock = new CountDownLatch(1);
        assertSuccess.setLock(lock);
        remoteHttpPostCalls.postCallWithBearerAuthInHeader(assertSuccess, "dummy_token", REQUEST_BODY);
        assertTrue(lock.await(DELAY_1000_MILLIS, MILLISECONDS));
    }
}
