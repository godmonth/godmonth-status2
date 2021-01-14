package com.godmonth.status2.builder.executor;

import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * <p></p >
 *
 * @author shenyue
 */
@DependsOn("entityManager")
public class OrderExecutorBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Class underlyClass = null;
        try {
            underlyClass = Class.forName(importingClassMetadata.getClassName());
        } catch (ClassNotFoundException e) {
            throw new ContextedRuntimeException(e);
        }

        final Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(OrderExecutor.class.getName());

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName(OrderExecutorFactoryBean.class.getName());
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue("annotationAttributes", annotationAttributes);
        values.addPropertyValue("underlyClass", underlyClass);
        beanDefinition.setPropertyValues(values);
        final String beanName = (String) annotationAttributes.get("beanName");
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

}
