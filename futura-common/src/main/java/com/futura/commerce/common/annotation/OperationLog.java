package com.futura.commerce.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for logging system operations via AOP
 *
 * @author Vitalii
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLog {
    String module();
    String operationType();
    String content() default "";
    String businessIdParam() default "";
}
