package com.godmonth.status2.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>标记status字段</p >
 *
 * @author shenyue
 */
@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Documented
public @interface Status {

    /**
     * triggerClass
     *
     * @return
     */
    Class triggerClass();
}
