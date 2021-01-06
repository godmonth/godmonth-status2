package com.godmonth.status.annotations.binding;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>既可以用于StatusAdvancer2，也可以用于StatusEntry</p >
 *
 * @author shenyue
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StatusBinding {

    /**
     * 只能是string或者枚举
     *
     * @return
     */
    Class statusClass() default String.class;

    String statusValue();


}
