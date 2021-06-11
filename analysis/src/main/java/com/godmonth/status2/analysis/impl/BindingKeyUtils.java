package com.godmonth.status2.analysis.impl;

import com.godmonth.status2.annotations.binding.InstructionBinding;
import com.godmonth.status2.annotations.binding.StatusBinding;
import com.godmonth.status2.annotations.utils.AnnotationValueUtils;
import com.godmonth.status2.annotations.utils.InstructionBindingUtils;
import com.godmonth.status2.annotations.utils.StatusBindingUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;

/**
 * <p></p >
 *
 * @author shenyue
 */
public class BindingKeyUtils {
    private BindingKeyUtils() {
    }

    public static Object[] getBindingKey(Class componentClass) {
        return getBindingKey(componentClass, null, null);
    }


    /**
     * @param componentClass 推进器类或者是entry类
     * @param statusField
     * @param instField
     * @return 如果没有instField，返回的是status list. 如果有instField返回的是pair list
     */
    public static Object[] getBindingKey(Class componentClass, AnnotationField statusField, AnnotationField instField) {
        final Object[] statusKeyPart = getStatusPart(componentClass, statusField);
        Validate.notNull(statusKeyPart, "statusBindingKeyPart is null");
        Object instKeyPart = getInstructionBindingKeyPart(componentClass, instField);
        Object[] bindKeyArray = new Object[statusKeyPart.length];
        if (instKeyPart != null) {
            for (int i = 0; i < statusKeyPart.length; i++) {
                bindKeyArray[i] = Pair.of(statusKeyPart[i], instKeyPart);
            }
            return bindKeyArray;
        }
        return statusKeyPart;
    }

    private static Object[] getStatusPart(Class componentClass, AnnotationField statusField) {
        Object[] keyPart = getAnnotationValueArray(componentClass, statusField);
        if (keyPart != null) {
            return keyPart;
        }
        StatusBinding statusBinding = AnnotationUtils.findAnnotation(componentClass, StatusBinding.class);
        if (statusBinding != null) {
            return StatusBindingUtils.parseStatusValues(statusBinding);
        }
        throw new IllegalArgumentException("binding key status part is null");
    }


    private static Object getInstructionBindingKeyPart(Class componentClass, AnnotationField statusField) {
        Object keyPart = getAnnotationValue(componentClass, statusField);
        if (keyPart != null) {
            return keyPart;
        }
        InstructionBinding instructionBinding = AnnotationUtils.findAnnotation(componentClass, InstructionBinding.class);
        if (instructionBinding != null) {
            keyPart = InstructionBindingUtils.parseInstructionValue(instructionBinding);
        }
        return keyPart;
    }

    /**
     * 查询类的标签值
     *
     * @param componentClass
     * @param annotationField
     * @return
     */
    private static Object getAnnotationValue(Class componentClass, AnnotationField annotationField) {
        if (annotationField != null) {
            Annotation annotation = AnnotationUtils.findAnnotation(componentClass, annotationField.getAnnoClass());
            if (annotation != null) {
                return AnnotationValueUtils.getValue(annotation, annotationField.getPropertyName());
            }
        }
        return null;
    }

    /**
     * 查询类的标签的数组值
     *
     * @param componentClass
     * @param annotationField
     * @return
     */
    private static Object[] getAnnotationValueArray(Class componentClass, AnnotationField annotationField) {
        if (annotationField != null) {
            Annotation annotation = AnnotationUtils.findAnnotation(componentClass, annotationField.getAnnoClass());
            if (annotation != null) {
                return AnnotationValueUtils.getArrayValue(annotation, annotationField.getPropertyName());
            }
        }
        return null;
    }

}
