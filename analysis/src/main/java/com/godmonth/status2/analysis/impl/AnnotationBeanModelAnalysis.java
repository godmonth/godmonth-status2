package com.godmonth.status2.analysis.impl;

import com.godmonth.status2.annotations.Status;
import lombok.Builder;
import lombok.Singular;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 使用Status注释来寻找status字段
 *
 * @param <MODEL>
 */
@ToString(callSuper = true)
public class AnnotationBeanModelAnalysis<MODEL> extends SimpleBeanModelAnalysis<MODEL> {

    protected Function<Class, Object> bindingKeyFunction;


    public AnnotationBeanModelAnalysis(Class<MODEL> modelClass) {
        this(modelClass, null);
    }

    @Builder(builderMethodName = "annoBuilder")
    public AnnotationBeanModelAnalysis(Class<MODEL> modelClass, @Singular("predicate") List<Predicate<MODEL>> predicateList) {
        this.modelClass = modelClass;
        this.predicateList = predicateList;

        final Pair<Field, Status> pair = getStatusField(modelClass);
        if (pair != null) {
            this.statusPropertyName = pair.getLeft().getName();
            this.statusClass = pair.getLeft().getType();
            this.triggerClass = pair.getRight().triggerClass();
            return;
        }
        final Pair<Method, Status> pair2 = getStatusByGetterMethod(modelClass);
        if (pair2 != null && pair2.getLeft().getName().startsWith("get")) {
            this.statusPropertyName = StringUtils.uncapitalize(StringUtils.substringAfter(pair2.getLeft().getName(), "get"));
            this.statusClass = pair2.getLeft().getReturnType();
            this.triggerClass = pair2.getRight().triggerClass();
        } else {
            throw new IllegalArgumentException("AnnotationBeanModelAnalysis failure.");
        }

    }

    private static Pair<Method, Status> getStatusByGetterMethod(Class modelClass) {
        final Method[] methodsWithAnnotation = MethodUtils.getMethodsWithAnnotation(modelClass, Status.class);
        for (Method method : methodsWithAnnotation) {
            Status annotation = method.getAnnotation(Status.class);
            if (annotation != null) {
                return Pair.of(method, annotation);
            }
        }
        return null;
    }

    private static Pair<Field, Status> getStatusField(Class modelClass) {
        Field[] fields = FieldUtils.getAllFields(modelClass);
        for (Field field : fields) {
            Status annotation = field.getAnnotation(Status.class);
            if (annotation != null) {
                return Pair.of(field, annotation);
            }
        }
        return null;
    }

}
