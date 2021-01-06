package com.godmonth.status2.annotations.utils;

import com.godmonth.status2.annotations.binding.InstructionBinding;

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
