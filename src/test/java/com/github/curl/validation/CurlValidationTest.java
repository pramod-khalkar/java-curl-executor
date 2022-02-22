package com.github.curl.validation;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.curl.ValidatorInterface;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.github.curl.CurlBuilder;
import io.github.curl.CurlCallBack;
import io.github.curl.CurlCallBackStream;
import io.github.curl.CurlResponse;
import io.github.curl.CurlStreamResponse;
import org.junit.jupiter.api.Test;

/**
 * @author : Pramod Khalkar
 * @since : 22/02/22, Tue
 * description: This file belongs to java-curl-executor
 **/
@WireMockTest(httpPort = 8080)
public class CurlValidationTest {

    @Test
    public void placeholder_missing_in_command() {
        Exception ex = assertThrows(RuntimeException.class, () -> {
            CurlBuilder.build(ValidatorInterface.PlaceHolderMissed.class);
        });
        assertTrue(ex.getMessage().contains("parameter count mismatch"));
    }

    @Test
    public void method_parameter_missed() {
        Exception ex = assertThrows(RuntimeException.class, () -> {
            CurlBuilder.build(ValidatorInterface.ParameterMissed.class);
        });
        assertTrue(ex.getMessage().contains("parameter count mismatch"));
    }

    @Test
    public void unexpected_method_return_type() {
        Exception ex = assertThrows(RuntimeException.class, () -> {
            CurlBuilder.build(ValidatorInterface.UnexpectedReturnType.class);
        });
        assertTrue(ex.getMessage().contains("in case of callback return type should be void"));
    }

    @Test
    public void neither_return_or_callback_available() {
        Exception ex = assertThrows(RuntimeException.class, () -> {
            CurlBuilder.build(ValidatorInterface.NeitherReturnOrCallbackAvailable.class);
        });
        String expected = String.format("neither callback(%s or %s) or return type (%s or %s)",
                CurlCallBack.class.getSimpleName(),
                CurlCallBackStream.class.getSimpleName(),
                CurlResponse.class.getSimpleName(),
                CurlStreamResponse.class.getSimpleName());
        assertTrue(ex.getMessage().contains(expected));
    }

    @Test
    public void not_even_single_method_annotated() {
        Exception ex = assertThrows(RuntimeException.class, () -> {
            CurlBuilder.build(ValidatorInterface.Empty.class);
        });
        assertTrue(ex.getMessage().contains("At least one method should have"));
    }

    @Test
    public void only_interface_allowed() {
        Exception ex = assertThrows(RuntimeException.class, () -> {
            CurlBuilder.build(ValidatorInterface.class);
        });
        assertTrue(ex.getMessage().contains("should be interface"));
    }

    @Test
    public void callBack_should_be_first_parameter() {
        Exception ex = assertThrows(RuntimeException.class, () -> {
            CurlBuilder.build(ValidatorInterface.CallbackShouldBeFirstMethodParameter.class);
        });
        assertTrue(ex.getMessage().contains("should be first parameter"));
    }

    @Test
    public void callBack_should_not_be_method_parameter() {
        Exception ex = assertThrows(RuntimeException.class, () -> {
            CurlBuilder.build(ValidatorInterface.CallbackShouldNotBeMultipleMethodsParameters.class);
        });
        assertTrue(ex.getMessage().contains("no multiple allowed"));
    }

    @Test
    public void interface_should_be_public() {
        Exception ex = assertThrows(RuntimeException.class, () -> {
            CurlBuilder.build(NonPublicInterface.class);
        });
        assertTrue(ex.getMessage().contains("should be public interface"));
    }
}
