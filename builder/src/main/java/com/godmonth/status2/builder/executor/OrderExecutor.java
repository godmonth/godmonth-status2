package com.godmonth.status2.builder.executor;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

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
@Import({ OrderExecutorBeanDefinitionRegistrar.class})
@Order
public @interface OrderExecutor {

    String beanName();

    /**
     * 默认下根据类型获得
     *
     * @return
     */
    String stateMachineAnalysisRef() default "";

    /**
     * 默认同级package下
     *
     * @return
     */
    String[] advancerBasePackages() default {};

    /**
     * 默认同级package下
     *
     * @return
     */
    String[] entryBasePackages() default {};

    /**
     * 默认根据类型获得
     *
     * @return
     */
    String transactionOperationsRef() default "";

    /**
     * 默认根据类型获得
     *
     * @return
     */
    String entityManagerRef() default "";

    /**
     * 状态机器定义资源路径
     *
     * @return
     */
    String stateMachineDefinitionResource();
}
