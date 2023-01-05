package com.godmonth.status2.builder.executor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * <p></p >
 *
 * @author shenyue
 */
@Slf4j
public class OrderExecutorBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, BeanClassLoaderAware {
    private ClassLoader classLoader;

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Class underlyClass = null;
        try {
            log.trace("underlyClass:{}", importingClassMetadata.getClassName());
            underlyClass = classLoader.loadClass(importingClassMetadata.getClassName());
        } catch (ClassNotFoundException e) {
            throw new ContextedRuntimeException(e);
        }

        final Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(OrderExecutor.class.getName());
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName(OrderExecutorFactoryBean.class.getName());
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue("annotationAttributes", annotationAttributes);
        values.addPropertyValue("underlyClass", underlyClass);
        values.addPropertyValue("classLoader", classLoader);
        beanDefinition.setPropertyValues(values);
        final String beanName = (String) annotationAttributes.get("beanName");
        log.debug("executor beanName:{}", beanName);
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
