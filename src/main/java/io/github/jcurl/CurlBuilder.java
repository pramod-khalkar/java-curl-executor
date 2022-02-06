package io.github.jcurl;

import static io.github.jcurl.Helper.formatSpecifierCount;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * Date: 03/02/22
 * Time: 4:33 pm
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
public class CurlBuilder {
    // TODO: 06/02/22 method return type should be string
    // TODO: 06/02/22 interface must be public
    public static <T> T build(Class<T> interfaceClazz) {
        if (!interfaceClazz.isInterface()) {
            throw new RuntimeException(String.format("%s should be interface", interfaceClazz.getSimpleName()));
        }
        List<Method> curlMethods = new ArrayList<>();
        for (Method method : interfaceClazz.getDeclaredMethods()) {
            Curl curlAnnotations = method.getAnnotation(Curl.class);
            if (curlAnnotations != null) {
                long callBackInstanceCount = Arrays.stream(method.getParameters()).filter(param -> param.getType() == CurlCallBack.class).count();
                if (callBackInstanceCount > 1 || (callBackInstanceCount == 1 && method.getParameters()[0].getType() != CurlCallBack.class)) {
                    throw new RuntimeException(
                            String.format("%s should be first parameter with no multiple allowed", CurlCallBack.class.getSimpleName()));
                }
                int methodTotalParam = callBackInstanceCount != 0 ? method.getParameterCount() - 1 : method.getParameterCount();
                int placeHolderCount = formatSpecifierCount(curlAnnotations.cmd());
                if (placeHolderCount != methodTotalParam) {
                    throw new RuntimeException(String.format("%s() parameter count mismatch", method.getName()));
                }
                curlMethods.add(method);
            }
        }
        if (curlMethods.size() == 0) {
            throw new RuntimeException(String.format("At least one method should have @%s", Curl.class.getSimpleName()));
        }
        return generateCode(interfaceClazz);
    }

    private static <T> T generateCode(Class<T> clazz) {
        try {
            Class<?> clazzImpl = new ByteBuddy()
                    .subclass(Object.class)
                    .implement(clazz)
                    .method(ElementMatchers.declaresAnnotation(ElementMatchers.annotationType(Curl.class)))
                    .intercept(MethodDelegation.withDefaultConfiguration().to(Interceptor.class))
                    .make()
                    .load(clazz.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                    .getLoaded();
            return (T) clazzImpl.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static class Interceptor {
        @RuntimeType
        public static Object intercept(@This Object proxy, @Origin Method method, @AllArguments Object[] args) throws Throwable {
            Annotation[] curlAnnotation = method.getAnnotationsByType(Curl.class);
            for (Annotation ant : curlAnnotation) {
                Curl curlAnt = (Curl) ant;
                if (args.length > 0 && args[0] instanceof CurlCallBack) {
                    String formattedCmd = String.format(curlAnt.cmd(), Arrays.copyOfRange(args, 1, args.length));
                    CurlCallBack callBackInstance = (CurlCallBack) args[0];
                    CurlParser.parse(formattedCmd).execute().response(callBackInstance);
                } else {
                    String formattedCmd = String.format(curlAnt.cmd(), args);
                    return CurlParser.parse(formattedCmd).execute().response();
                }
            }
            return null;
        }
    }
}
