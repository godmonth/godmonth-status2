package com.godmonth.status.annotations.binding;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 非空指令
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InstructionBinding {
    /**
     * 只能是string或者枚举
     *
     * @return
     */
    Class instructionClass();

    String instructionValue();
}
