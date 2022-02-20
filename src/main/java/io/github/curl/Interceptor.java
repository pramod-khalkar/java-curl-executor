package io.github.curl;

import java.lang.reflect.Method;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

/**
 * Date: 17/02/22
 * Time: 10:41 PM
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
public interface Interceptor {
    @RuntimeType
    Object intercept(@This Object proxy, @Origin Method method, @AllArguments Object[] args) throws Exception;
}
