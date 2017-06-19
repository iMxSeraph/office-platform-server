package io.muxin.office.server.config.annotation;

import io.muxin.office.server.common.constant.AuthType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 鉴权注解
 * Created by muxin on 2017/2/25.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authentication {
    AuthType value() default AuthType.USER;
}
