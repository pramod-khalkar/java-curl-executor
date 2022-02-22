package com.github.curl.execution;

import static com.github.curl.utils.TestConstant.DELAY_1000_MILLIS;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.noContent;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.curl.RemoteHttpDeleteCalls;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.github.curl.CurlBuilder;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.Test;

/**
 * @author : Pramod Khalkar
 * @since : 22/02/22, Tue
 * description: This file belongs to java-curl-executor
 **/
@WireMockTest(httpPort = 8080)
public class DeleteCmdExeTest {
    private final RemoteHttpDeleteCalls remoteHttpDeleteCalls = CurlBuilder.build(RemoteHttpDeleteCalls.class);
    CallBackAssertion assertSuccess = new CallBackAssertion(204, "", null, true);

    @Test
    public void simple_delete_call() throws InterruptedException {
        stubFor(delete("/api/test")
                .willReturn(noContent()));
        CountDownLatch lock = new CountDownLatch(2);
        assertSuccess.setLock(lock);
        remoteHttpDeleteCalls.simpleDeleteCall_1(assertSuccess);
        remoteHttpDeleteCalls.simpleDeleteCall_2(assertSuccess);
        assertTrue(lock.await(DELAY_1000_MILLIS, MILLISECONDS));
    }

    @Test
    public void simple_delete_call_with_id_in_path() throws InterruptedException {
        stubFor(delete(urlPathMatching("/api/test/([0-9]*)"))
                .willReturn(noContent()));
        CountDownLatch lock = new CountDownLatch(1);
        assertSuccess.setLock(lock);
        remoteHttpDeleteCalls.simpleDeleteCall_3(assertSuccess, "1234");
        assertTrue(lock.await(DELAY_1000_MILLIS, MILLISECONDS));
    }
}
