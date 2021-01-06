package com.godmonth.status.annotations.utils;

import org.apache.commons.lang3.exception.ContextedRuntimeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p></p >
 *
 * @author shenyue
 */

public class AnnotationValueUtils {
    private AnnotationValueUtils() {
    }

    public static Object getValue(Annotation annotation, String valueParam) {
        try {
            Method valueMethod = annotation.getClass().getMethod(valueParam);
            return valueMethod.invoke(annotation);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new ContextedRuntimeException(e);
        }
    }

    public static Object parseEnumValue(Annotation annotation, String valueClassParam, String valueParam) {
        try {
            Method valueClassMethod = annotation.getClass().getMethod(valueClassParam);
            Class valueClass = (Class) valueClassMethod.invoke(annotation);
            Method valueMethod = annotation.getClass().getMethod(valueParam);
            String value = (String) valueMethod.invoke(annotation);
            if (String.class.equals(valueClass)) {
                return value;
            } else if (valueClass.isEnum()) {
                return Enum.valueOf(valueClass, value);
            } else {
                throw new IllegalArgumentException("status type only supports string or enum:" + valueClass);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new ContextedRuntimeException(e);
        }
    }
}
