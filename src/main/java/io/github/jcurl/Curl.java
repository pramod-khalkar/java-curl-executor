package io.github.jcurl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Date: 03/02/22
 * Time: 4:32 pm
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Curl {
    String cmd();
}
