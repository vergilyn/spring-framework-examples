package com.vergilyn.examples.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author VergiLyn
 * @date 2017/3/19
 * @see lombok.extern.slf4j.Slf4j
 */
@Retention(RUNTIME)
@Target(FIELD)
@Documented
@Inherited
public @interface Log {

}