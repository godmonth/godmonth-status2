package com.godmonth.status2.builder.binding;

import com.godmonth.status2.analysis.impl.BindingKeyUtils;
import com.godmonth.status2.annotations.binding.ModelBinding;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p></p >
 *
 * @author shenyue
 */
@Slf4j
public class BindingListBuilder {
    @Builder
    private static <T> List<Pair<Object, T>> build(ClassLoader classLoader, @NonNull @Singular Set<String> packageNames, Class enableAnnotationClass, Class ancestorClass, Class modelClass, Predicate<Class> predicate, Function<Class, Object[]> bindingKeyFunction, @NonNull AutowireCapableBeanFactory autowireCapableBeanFactory) throws IOException, ClassNotFoundException {
        Validate.notEmpty(packageNames, "packageNames is empty");
        bindingKeyFunction = bindingKeyFunction != null ? bindingKeyFunction : BindingKeyUtils::getBindingKey;
        List<Pair<Object, T>> list = new ArrayList<>();
        ClassPath classPath = ClassPath.from(classLoader != null ? classLoader : ClassLoader.getSystemClassLoader());
        for (String packageName : packageNames) {
            ImmutableSet<ClassPath.ClassInfo> topLevelClasses = classPath.getTopLevelClassesRecursive(packageName);
            for (ClassPath.ClassInfo topLevelClass : topLevelClasses) {
                Class<?> aClass = Class.forName(topLevelClass.getName());

                //检查激活标志
                if (enableAnnotationClass != null && AnnotationUtils.findAnnotation(aClass, enableAnnotationClass) == null) {
                    continue;
                }

                //检查父类
                if (ancestorClass != null && !ancestorClass.isAssignableFrom(aClass)) {
                    continue;
                }

                //匹配模型
                if (modelClass != null) {
                    ModelBinding modelBinding = AnnotationUtils.findAnnotation(aClass, ModelBinding.class);
                    if (modelBinding != null && !modelClass.equals(modelBinding.value())) {
                        continue;
                    }
                }

                //过滤断言
                if (predicate != null && !predicate.test(aClass)) {
                    continue;
                }
                final List<Pair<Object, T>> byAnnotation = createByAnnotation(aClass, autowireCapableBeanFactory, bindingKeyFunction);
                if (byAnnotation != null) {
                    list.addAll(byAnnotation);
                }
            }
        }
        log.debug("binding list:{}", list);
        return list;
    }

    public static <T> List<Pair<Object, T>> createByAnnotation(Class aClass, AutowireCapableBeanFactory autowireCapableBeanFactory, Function<Class, Object[]> bindingKeyFunction) {
        Object[] keys = bindingKeyFunction.apply(aClass);
        List<Pair<Object, T>> pairs = new ArrayList<>();
        if (keys != null) {
            T component = (T) autowireCapableBeanFactory.autowire(aClass, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
            for (int i = 0; i < keys.length; i++) {
                Object key = keys[i];
                if (component != null) {
                    final Pair<Object, T> of = Pair.of(key, component);
                    pairs.add(of);
                }
            }
        }
        return pairs;
    }
}
