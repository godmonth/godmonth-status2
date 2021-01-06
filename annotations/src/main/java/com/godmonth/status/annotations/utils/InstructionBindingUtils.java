package com.godmonth.status.annotations.utils;

import com.godmonth.status.annotations.binding.InstructionBinding;

/**
 * <p></p >
 *
 * @author shenyue
 */
public class InstructionBindingUtils {
    private InstructionBindingUtils() {
    }

    public static Object parseInstructionValue(InstructionBinding annotation) {
        return AnnotationValueUtils.parseEnumValue(annotation, "instructionClass", "instructionValue");
    }
}
