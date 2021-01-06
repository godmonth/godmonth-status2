package com.godmonth.status2.analysis.impl;

import com.godmonth.status2.annotations.Status;
import lombok.Builder;
import lombok.Singular;
import lombok.ToString;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Predicate;

/**
 * 使用Status注释来寻找status字段
 *
 * @param <MODEL>
 */
@ToString(callSuper = true)
public class AnnotationBeanModelAnalysis<MODEL> extends SimpleBeanModelAnalysis<MODEL> {

    public AnnotationBeanModelAnalysis(Class<MODEL> modelClass) {
        this(modelClass, null);
    }

    @Builder(builderMethodName = "annoBuilder")
    public AnnotationBeanModelAnalysis(Class<MODEL> modelClass, @Singular("predicate") List<Predicate<MODEL>> predicateList) {
        this.modelClass = modelClass;
        this.predicateList = predicateList;

        final Pair<Field, Status> pair = getStatusField(modelClass);
        this.statusPropertyName = pair.getLeft().getName();
        this.statusClass = pair.getLeft().getType();
        this.triggerClass = pair.getRight().triggerClass();
    }

    private static Pair<Field, Status> getStatusField(Class modelClass) {
        Field[] fields = FieldUtils.getAllFields(modelClass);
        for (Field field : fields) {
            Status annotation = field.getAnnotation(Status.class);
            if (annotation != null) {
                return Pair.of(field, annotation);
            }
        }
        throw new IllegalArgumentException("statusPropertyName is null.");
    }

}
