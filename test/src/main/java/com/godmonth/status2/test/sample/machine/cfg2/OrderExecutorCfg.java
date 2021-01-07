package com.godmonth.status2.test.sample.machine.cfg2;

import org.springframework.stereotype.Component;

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
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface OrderExecutorCfg {

    String modelAnalysisRef() default "";

    String[] advancerBasePackages() default {};


}
