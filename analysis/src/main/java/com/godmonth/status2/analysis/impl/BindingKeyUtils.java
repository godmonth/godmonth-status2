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
        Object[] keyPart = getBindingKeyArrayPart(componentClass, statusField);
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
        Object keyPart = getBindingKeyPart(componentClass, statusField);
        if (keyPart != null) {
            return keyPart;
        }
        InstructionBinding instructionBinding = AnnotationUtils.findAnnotation(componentClass, InstructionBinding.class);
        if (instructionBinding != null) {
            keyPart = InstructionBindingUtils.parseInstructionValue(instructionBinding);
        }
        return keyPart;
    }

    private static Object getBindingKeyPart(Class componentClass, AnnotationField statusField) {
        if (statusField != null) {
            Annotation annotation = AnnotationUtils.findAnnotation(componentClass, statusField.getAnnoClass());
            if (annotation != null) {
                return AnnotationValueUtils.getValue(annotation, statusField.getPropertyName());
            }
        }
        return null;
    }

    private static Object[] getBindingKeyArrayPart(Class componentClass, AnnotationField statusField) {
        if (statusField != null) {
            Annotation annotation = AnnotationUtils.findAnnotation(componentClass, statusField.getAnnoClass());
            if (annotation != null) {
                return AnnotationValueUtils.getArrayValue(annotation, statusField.getPropertyName());
            }
        }
        return null;
    }

}
