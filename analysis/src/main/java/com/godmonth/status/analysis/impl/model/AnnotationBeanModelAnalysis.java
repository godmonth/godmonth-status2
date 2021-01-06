package com.godmonth.status.analysis.impl.model;

import com.godmonth.status.annotations.Status;
import lombok.Builder;
import lombok.ToString;
import org.apache.commons.lang3.reflect.FieldUtils;

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
    public AnnotationBeanModelAnalysis(Class<MODEL> modelClass, List<Predicate<MODEL>> predicateList) {
        super(modelClass, getStatusPropertyName(modelClass), predicateList);
    }

    private static String getStatusPropertyName(Class modelClass) {
        Field[] fields = FieldUtils.getAllFields(modelClass);
        for (Field field : fields) {
            Status annotation = field.getAnnotation(Status.class);
            if (annotation != null) {
                return field.getName();
            }
        }
        throw new IllegalArgumentException("statusPropertyName is null.");
    }


}
