package io.github.curl;

import static io.github.curl.Helper.formatSpecifierCount;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
 * @author : Pramod Khalkar
 * @since : 22/02/22, Tue
 * description: This file belongs to java-curl-executor
 **/
public final class CurlBuilder {
    private CurlBuilder() {
    }

    /**
     * @param interfaceClazz : interface which has curl command's defined
     * @param <T>            : return curl cmd implementations class
     * @return : implementation will return for curl commands defined in interface
     */
    public static <T> T build(Class<T> interfaceClazz) {
        //provided class should be interface only
        if (!interfaceClazz.isInterface()) {
            throw new RuntimeException(String.format("%s should be interface", interfaceClazz.getSimpleName()));
        }
        if (!Modifier.isPublic(interfaceClazz.getModifiers())) {
            throw new RuntimeException(String.format("%s should be public interface", interfaceClazz.getSimpleName()));
        }
        List<Method> curlMethods = new ArrayList<>();
        for (Method method : interfaceClazz.getDeclaredMethods()) {
            Curl curlAnnotations = method.getAnnotation(Curl.class);
            if (curlAnnotations != null) {
                //Callback sequence/multiple instances check for callback parameters
                long callBackInstanceCount = Arrays.stream(method.getParameters())
                        .filter(param -> param.getType() == CurlCallBack.class || param.getType() == CurlCallBackStream.class)
                        .count();
                if (callBackInstanceCount > 1 || (callBackInstanceCount == 1 && (method.getParameters()[0].getType() != CurlCallBack.class &&
                        method.getParameters()[0].getType() != CurlCallBackStream.class))) {
                    throw new RuntimeException(
                            String.format("%s should be first parameter with no multiple allowed", CurlCallBack.class.getSimpleName()));
                }
                int methodTotalParam = callBackInstanceCount != 0 ? method.getParameterCount() - 1 : method.getParameterCount();
                int placeHolderCount = formatSpecifierCount(curlAnnotations.cmd());
                if (placeHolderCount != methodTotalParam) {
                    throw new RuntimeException(String.format("%s() parameter count mismatch", method.getName()));
                }

                //Checking return type constraint
                boolean isValidReturnTypeAvail = method.getReturnType() == CurlResponse.class
                        || method.getReturnType() == CurlStreamResponse.class;
                if (callBackInstanceCount > 0 && (isValidReturnTypeAvail || !method.getReturnType().equals(Void.TYPE))) {
                    throw new RuntimeException(String.format("for %s in case of callback return type should be void",
                            method.getName()));
                }
                if (callBackInstanceCount == 0 && !isValidReturnTypeAvail) {
                    throw new RuntimeException(String.format("for %s neither callback(%s or %s) or return type (%s or %s) supplied",
                            method.getName(),
                            CurlCallBack.class.getSimpleName(),
                            CurlCallBackStream.class.getSimpleName(),
                            CurlResponse.class.getSimpleName(),
                            CurlStreamResponse.class.getSimpleName()));
                }

                curlMethods.add(method);
            }
        }
        //Annotated method count/at least one method should be annotated in interface
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
                    .intercept(MethodDelegation.withDefaultConfiguration().to(new Interceptor() {
                        @RuntimeType
                        public Object intercept(@This Object proxy, @Origin Method method, @AllArguments Object[] args) throws Exception {
                            return interceptMethod(proxy, method, args);
                        }
                    }, Interceptor.class))
                    .make()
                    .load(clazz.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                    .getLoaded();
            return (T) clazzImpl.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static Object interceptMethod(Object proxy, Method method, Object[] args) throws Exception {
        Annotation[] curlAnnotation = method.getAnnotationsByType(Curl.class);
        for (Annotation ant : curlAnnotation) {
            Curl curlAnt = (Curl) ant;
            if (args.length > 0 && (args[0] instanceof CurlCallBack || args[0] instanceof CurlCallBackStream)) {
                String formattedCmd = String.format(curlAnt.cmd(), Arrays.copyOfRange(args, 1, args.length));
                if (args[0] instanceof CurlCallBack) {
                    CurlCallBack callBack = (CurlCallBack) args[0];
                    CurlParser.parse(formattedCmd).execute(callBack);
                } else {
                    CurlCallBackStream callBack = (CurlCallBackStream) args[0];
                    CurlParser.parse(formattedCmd).execute(callBack);
                }
            } else {
                String formattedCmd = String.format(curlAnt.cmd(), args);
                if (method.getReturnType() == CurlResponse.class) {
                    return CurlParser.parse(formattedCmd).execute().response();
                } else if (method.getReturnType() == CurlStreamResponse.class) {
                    return CurlParser.parse(formattedCmd).execute().streamResponse();
                }
            }
        }
        return null;
    }
}
