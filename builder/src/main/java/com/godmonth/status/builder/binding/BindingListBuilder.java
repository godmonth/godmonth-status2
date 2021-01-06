package com.godmonth.status.builder.binding;

import com.godmonth.status.annotations.binding.ModelBinding;
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
    private static <T> List<Pair<Object, T>> build(ClassLoader classLoader, @NonNull @Singular Set<String> packageNames, Class enableAnnotationClass, Class ancestorClass, Class modelClass, Predicate<Class> predicate, Function<Class, Object> keyFinder, @NonNull AutowireCapableBeanFactory autowireCapableBeanFactory) throws IOException, ClassNotFoundException {
        Validate.notEmpty(packageNames, "packageNames is empty");
        keyFinder = keyFinder != null ? keyFinder : BindingKeyUtils::getBindingKey;
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

                Pair<Object, T> pair = createByAnnotation(aClass, autowireCapableBeanFactory, keyFinder);
                if (pair != null) {
                    list.add(pair);
                }
            }
        }
        log.debug("binding list:{}", list);
        return list;
    }

    public static <T> Pair<Object, T> createByAnnotation(Class aClass, AutowireCapableBeanFactory autowireCapableBeanFactory, Function<Class, Object> keyFinder) {
        Object key = keyFinder.apply(aClass);
        if (key != null) {
            T component = (T) autowireCapableBeanFactory.autowire(aClass, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
            if (component != null) {
                return Pair.of(key, component);
            }
        }
        return null;
    }
}
