package com.godmonth.status2.builder.binding;

import com.godmonth.status2.analysis.impl.BindingKeyUtils;
import com.godmonth.status2.annotations.binding.ModelBinding;
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
    /**
     * @param classLoader
     * @param packageNames
     * @param enableAnnotationClass
     * @param ancestorClass
     * @param modelClass
     * @param predicate
     * @param bindingKeyFunction
     * @param autowireCapableBeanFactory
     * @param <T>                        推进器类
     * @return Object是路由key
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Builder
    private static <T> List<Pair<Object, T>> build(@NonNull ClassLoader classLoader, @NonNull @Singular Set<String> packageNames, Class enableAnnotationClass, Class ancestorClass, Class modelClass, Predicate<Class> predicate, Function<Class, Object[]> bindingKeyFunction, @NonNull AutowireCapableBeanFactory autowireCapableBeanFactory) throws IOException, ClassNotFoundException {
        log.trace("start building list:{},packageNames:{}", enableAnnotationClass, packageNames);
        Validate.notEmpty(packageNames, "packageNames is empty");
        bindingKeyFunction = bindingKeyFunction != null ? bindingKeyFunction : BindingKeyUtils::getBindingKey;
        List<Pair<Object, T>> list = new ArrayList<>();

        for (String packageName : packageNames) {
            log.trace("packageName:{}", packageName);
            Set<Class<?>> topLevelClasses = TopClassesSearcher.searchTopClasses(packageName, classLoader);
            log.trace("topLevelClasses.size:{}", topLevelClasses.size());
            for (Class<?> topLevelClass : topLevelClasses) {
                //检查激活标志
                if (enableAnnotationClass != null && AnnotationUtils.findAnnotation(topLevelClass, enableAnnotationClass) == null) {
                    log.trace("skip:{}", topLevelClass);
                    continue;
                }

                //检查父类
                if (ancestorClass != null && !ancestorClass.isAssignableFrom(topLevelClass)) {
                    log.trace("skip:{}", topLevelClass);
                    continue;
                }

                //匹配模型
                if (modelClass != null) {
                    ModelBinding modelBinding = AnnotationUtils.findAnnotation(topLevelClass, ModelBinding.class);
                    if (modelBinding != null && !modelClass.equals(modelBinding.value())) {
                        log.trace("skip:{}", topLevelClass);
                        continue;
                    }
                }

                //过滤断言
                if (predicate != null && !predicate.test(topLevelClass)) {
                    log.trace("skip:{}", topLevelClass);
                    continue;
                }
                final List<Pair<Object, T>> byAnnotation = createByAnnotation(topLevelClass, autowireCapableBeanFactory, bindingKeyFunction);
                if (byAnnotation != null) {
                    log.trace("byAnnotation:{}", byAnnotation);
                    list.addAll(byAnnotation);
                }
            }
        }
        log.debug("finished binding list:{}", list);
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
