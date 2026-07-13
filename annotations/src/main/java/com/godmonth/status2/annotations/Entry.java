package com.godmonth.status2.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>激活状态入口</p >
 *
 * @author shenyue
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Entry {

    /**
     * 同一个状态对应多个 Entry 时，按 order 升序执行；数值越小越先执行；默认 0。
     */
    int order() default 0;
}
