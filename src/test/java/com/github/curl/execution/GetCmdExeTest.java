package com.github.curl.execution;

import static com.github.curl.utils.TestConstant.DELAY_1000_MILLIS;
import static com.github.curl.utils.TestConstant.RESPONSE_BODY;
import static com.github.curl.utils.TestConstant.TEST_USERNAME;
import static com.github.curl.utils.TestConstant.TEST_USER_PASSWORD;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.unauthorized;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.curl.RemoteHttpGetCalls;
import com.github.curl.utils.Helper;
import com.github.curl.validation.CurlValidationTest;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.github.curl.CurlBuilder;
import io.github.curl.CurlResponse;
import io.github.curl.CurlStreamResponse;
import io.github.curl.MimeTypes;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : Pramod Khalkar
 * @since : 22/02/22, Tue
 * description: This file belongs to java-curl-executor
 **/

@WireMockTest(httpPort = 8080)
public class GetCmdExeTest {
    private final static Logger log = LoggerFactory.getLogger(CurlValidationTest.class);
    private static final Helper helper = new Helper();
    private final RemoteHttpGetCalls remoteHttpGetCalls = CurlBuilder.build(RemoteHttpGetCalls.class);
    CallBackAssertion assertSuccess = new CallBackAssertion(200, RESPONSE_BODY.toString(), null, true);
    CallBackAssertion assertFail = new CallBackAssertion(-1, RESPONSE_BODY.toString(), null, true);

    @Test
    public void get_simple_call() throws InterruptedException {
        stubFor(get("/api/test")
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(RESPONSE_BODY.toString())));
        final CountDownLatch lock = new CountDownLatch(2);
        assertSuccess.setLock(lock);
        remoteHttpGetCalls.simpleGetCall_1(assertSuccess);
        remoteHttpGetCalls.simpleGetCall_3(assertSuccess);
        assertTrue(lock.await(DELAY_1000_MILLIS, MILLISECONDS));
    }

    @Test
    public void get_simple_call_sync() {
        stubFor(get("/api/test")
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(RESPONSE_BODY.toString())));
        CurlResponse response = remoteHttpGetCalls.simpleGetCall_2();
        assertEquals(200, response.getCode());
        assertEquals(RESPONSE_BODY.toString(), response.getBody());
    }

    @Test
    public void get_call_with_stream_of_responses() throws InterruptedException {
        stubFor(get("/api/test")
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(RESPONSE_BODY.toString())));
        CurlStreamResponse curlStreamResponse = remoteHttpGetCalls.simpleGetCallWithStreamOfResponse();
        assertEquals(200, curlStreamResponse.getCode());
        assertEquals(RESPONSE_BODY.toString(), Helper.readFromStream(curlStreamResponse.getInputStream()));

        final CountDownLatch lock = new CountDownLatch(1);
        assertSuccess.setLock(lock);
        remoteHttpGetCalls.simpleGetCallWithStreamOfResponse(assertSuccess);
        assertTrue(lock.await(DELAY_1000_MILLIS, MILLISECONDS));
    }


    @Test
    public void get_call_with_valid_header_parameter() throws InterruptedException {
        stubFor(get("/api/test")
                .withHeader("api-key", equalTo("1234"))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(RESPONSE_BODY.toString())));
        final CountDownLatch lock = new CountDownLatch(1);
        assertSuccess.setLock(lock);
        remoteHttpGetCalls.getCallWithHeader(assertSuccess);
        assertTrue(lock.await(DELAY_1000_MILLIS, MILLISECONDS));
    }

    @Test
    public void get_call_with_incorrect_header_parameter() throws InterruptedException {
        stubFor(get("/api/test")
                .withHeader("api-key", equalTo("wrong_key"))
                .willReturn(unauthorized()
                        .withHeader("Content-Type", "application/json")
                        .withBody(RESPONSE_BODY.editMessage("unauthorized").toString())));
        final CountDownLatch lock = new CountDownLatch(1);
        assertFail.setLock(lock);
        remoteHttpGetCalls.getCallWithIncorrectHeaderKey(assertFail
                .setExpectedCode(401)
                .setExpectedBody(RESPONSE_BODY.editMessage("unauthorized").toString()));
        assertTrue(lock.await(DELAY_1000_MILLIS, MILLISECONDS));
    }

    @Test
    public void get_call_with_basic_auth() throws InterruptedException {
        stubFor(get("/api/test")
                .withBasicAuth(TEST_USERNAME, TEST_USER_PASSWORD)
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(RESPONSE_BODY.toString())));
        final CountDownLatch lock = new CountDownLatch(6);
        assertSuccess.setLock(lock);
        remoteHttpGetCalls.getCallWithBasicAuthShortOption(assertSuccess);
        remoteHttpGetCalls.getCallWithBasicAuthLongOption(assertSuccess);
        remoteHttpGetCalls.getCallWithBasicAuthFlagSet_1(assertSuccess);
        remoteHttpGetCalls.getCallWithBasicAuthFlagSet_2(assertSuccess);
        String encodedCredentials = Base64.getEncoder().encodeToString("dummy_user:1234".getBytes(StandardCharsets.UTF_8));
        remoteHttpGetCalls.getCallWithBasicAuthDirectInHeader(assertSuccess, encodedCredentials);
        remoteHttpGetCalls.getCallWithBasicAuthDirectInHeaderNoSpace(assertSuccess, encodedCredentials);
        assertTrue(lock.await(DELAY_1000_MILLIS, MILLISECONDS));
    }

    @Test
    public void get_call_with_bearer_auth() throws InterruptedException {
        stubFor(get("/api/test")
                .withHeader("Authorization", equalTo("Bearer dummy_token"))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(RESPONSE_BODY.toString())));
        final CountDownLatch lock = new CountDownLatch(1);
        assertSuccess.setLock(lock);
        remoteHttpGetCalls.getCallWithBearerAuthDirectInHeader(assertSuccess, "dummy_token");
        assertTrue(lock.await(DELAY_1000_MILLIS, MILLISECONDS));
    }

    @Test
    public void sync_async_call_test() throws InterruptedException {
        stubFor(get("/api/test")
                .willReturn(ok()
                        .withFixedDelay(2000)
                        .withHeader("Content-Type", "application/json")
                        .withBody(RESPONSE_BODY.toString())));

        final CountDownLatch lock = new CountDownLatch(1);
        long asyncTakenTime = helper.calculateTime(() -> {
            remoteHttpGetCalls.AsyncCall(result -> {
                assertEquals(200, result.getCode());
                assertEquals(RESPONSE_BODY.toString(), result.getBody());
                log.info("AsyncCall response validated");
                lock.countDown();
            });
        });
        log.info("Callback task completed within {}ms", asyncTakenTime);

        long syncTakenTime = helper.calculateTime(() -> {
            CurlResponse syncResult = remoteHttpGetCalls.syncCall();
            assertEquals(200, syncResult.getCode());
            assertEquals(RESPONSE_BODY.toString(), syncResult.getBody());
            log.info("syncCall response validated");
        });

        log.info("Sync time: {}ms, Async time: {}ms", syncTakenTime, asyncTakenTime);
        assertTrue(asyncTakenTime < syncTakenTime);
        assertTrue(lock.await(3000, MILLISECONDS));
    }

    @Disabled
    @Test
    public void connection_timeout_test() {
        stubFor(get("/api/test")
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(RESPONSE_BODY.toString())));
        CurlResponse result = remoteHttpGetCalls.connectionTimeOutGetCall();
        assertEquals(-1, result.getCode());
        assertTrue(result.getBody().toLowerCase(Locale.ROOT).contains("timeout"));
    }

    @Test
    public void read_timeout_test() {
        stubFor(get("/api/test")
                .willReturn(ok()
                        .withFixedDelay(10000)
                        .withHeader("Content-Type", "application/json")
                        .withBody(RESPONSE_BODY.toString())));

        CurlResponse result = remoteHttpGetCalls.readTimeOutGetCall(2);
        assertEquals(-1, result.getCode());
        assertTrue(result.getBody().toLowerCase(Locale.ROOT).contains("read timed out"));
    }

    @Test
    public void txt_file_download_test() throws IOException {
        String filePath = "sample_data_file_1.txt";
        stubFor(get("/download")
                .willReturn(ok()
                        .withBodyFile(filePath)
                        .withHeader("Content-Type", "plain/text")));

        CurlStreamResponse curlStreamResponse = remoteHttpGetCalls.txtFileDownload();
        String contentType = curlStreamResponse.getHeaderFields().get("Content-Type").get(0);
        String ext = MimeTypes.getExt(contentType);
        File file = Helper.convertToFile(curlStreamResponse.getInputStream(), String.format("downloaded_text_file.%s", ext));
        assertEquals(200, curlStreamResponse.getCode());
        assertTrue(file.exists());
    }

    @Test
    public void img_file_download_test() throws IOException {
        String filePath = "image.png";
        stubFor(get("/download")
                .willReturn(ok()
                        .withBodyFile(filePath)
                        .withHeader("Content-Type", "image/png")));

        CurlStreamResponse curlStreamResponse = remoteHttpGetCalls.txtFileDownload();
        String contentType = curlStreamResponse.getHeaderFields().get("Content-Type").get(0);
        String ext = MimeTypes.getExt(contentType);
        File file = Helper.convertToFile(curlStreamResponse.getInputStream(), String.format("downloaded_img_file.%s", ext));
        assertEquals(200, curlStreamResponse.getCode());
        assertTrue(file.exists());
    }

    @Test
    public void pdf_file_download_test() throws IOException {
        String filePath = "sample_pdf_file.pdf";
        stubFor(get("/download")
                .willReturn(ok()
                        .withBodyFile(filePath)
                        .withHeader("Content-Type", "application/pdf")));

        CurlStreamResponse curlStreamResponse = remoteHttpGetCalls.txtFileDownload();
        String contentType = curlStreamResponse.getHeaderFields().get("Content-Type").get(0);
        String ext = MimeTypes.getExt(contentType);
        File file = Helper.convertToFile(curlStreamResponse.getInputStream(), String.format("downloaded_pdf_file.%s", ext));
        assertEquals(200, curlStreamResponse.getCode());
        assertTrue(file.exists());
    }
}
