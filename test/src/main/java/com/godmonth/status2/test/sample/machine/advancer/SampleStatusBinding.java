package com.godmonth.status2.test.sample.machine.advancer;

import com.godmonth.status2.test.sample.domain.SampleStatus;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p></p >
 *
 * @author shenyue
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SampleStatusBinding {
    SampleStatus[] value();
}
