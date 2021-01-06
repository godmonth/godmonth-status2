package com.godmonth.status.builder.binding;

import com.godmonth.status.annotations.binding.InstructionBinding;
import com.godmonth.status.annotations.binding.StatusBinding;
import com.godmonth.status.annotations.utils.AnnotationValueUtils;
import com.godmonth.status.annotations.utils.InstructionBindingUtils;
import com.godmonth.status.annotations.utils.StatusBindingUtils;
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

    public static Object getBindingKey(Class componentClass) {
        return getBindingKey(componentClass, null, null);
    }

    public static Object getBindingKey(Class componentClass, AnnotationField statusField, AnnotationField instField) {
        Object statusKeyPart = getStatusBindingKeyPart(componentClass, statusField);
        Validate.notNull(statusKeyPart, "statusBindingKeyPart is null");
        Object instKeyPart = getInstructionBindingKeyPart(componentClass, instField);
        if (instKeyPart != null) {
            return Pair.of(statusKeyPart, instKeyPart);
        }
        return statusKeyPart;
    }

    public static Object getStatusBindingKeyPart(Class componentClass, AnnotationField statusField) {
        Object keyPart = getBindingKeyPart(componentClass, statusField);
        if (keyPart != null) {
            return keyPart;
        }
        StatusBinding statusBinding = AnnotationUtils.findAnnotation(componentClass, StatusBinding.class);
        if (statusBinding != null) {
            keyPart = StatusBindingUtils.parseStatusValue(statusBinding);
        }
        return keyPart;
    }


    public static Object getInstructionBindingKeyPart(Class componentClass, AnnotationField statusField) {
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

    public static Object getBindingKeyPart(Class componentClass, AnnotationField statusField) {
        if (statusField != null) {
            Annotation annotation = AnnotationUtils.findAnnotation(componentClass, statusField.getAnnoClass());
            if (annotation != null) {
                return AnnotationValueUtils.getValue(annotation, statusField.getPropertyName());
            }
        }
        return null;
    }


}
