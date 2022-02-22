package com.github.curl.execution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.curl.utils.Helper;
import io.github.curl.CurlCallBack;
import io.github.curl.CurlCallBackStream;
import io.github.curl.CurlResponse;
import io.github.curl.CurlStreamResponse;
import java.util.concurrent.CountDownLatch;
import org.eclipse.jetty.http.HttpStatus;

/**
 * @author : Pramod Khalkar
 * @since : 22/02/22, Tue
 * description: This file belongs to java-curl-executor
 **/
class CallBackAssertion implements CurlCallBack, CurlCallBackStream {
    private CountDownLatch lock;
    private final boolean isOfSuccess;
    private int expectedCode;
    private String expectedBody;

    CallBackAssertion(int expectedCode, String expectedBody, CountDownLatch lock, boolean isOfSuccess) {
        this.lock = lock;
        this.isOfSuccess = isOfSuccess;
        this.expectedCode = expectedCode;
        this.expectedBody = expectedBody;
    }

    @Override
    public void onResult(CurlResponse result) {
        if (isOfSuccess) {
            assertEquals(expectedCode, result.getCode());
            assertEquals(expectedBody, result.getBody());
        } else {
            assertTrue(HttpStatus.isClientError(result.getCode()));
        }
        lock.countDown();
    }

    @Override
    public void onResult(CurlStreamResponse result) {
        if (isOfSuccess) {
            assertEquals(expectedCode, result.getCode());
            assertEquals(expectedBody, Helper.readFromStream(result.getInputStream()));
        } else {
            assertTrue(HttpStatus.isClientError(result.getCode()));
        }
        lock.countDown();
    }

    public void setLock(CountDownLatch lock) {
        this.lock = lock;
    }

    public CallBackAssertion setExpectedCode(int expectedCode) {
        this.expectedCode = expectedCode;
        return this;
    }

    public CallBackAssertion setExpectedBody(String expectedBody) {
        this.expectedBody = expectedBody;
        return this;
    }
}
