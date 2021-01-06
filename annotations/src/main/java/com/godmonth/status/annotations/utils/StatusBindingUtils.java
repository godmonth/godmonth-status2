package com.godmonth.status.annotations.utils;

import com.godmonth.status.annotations.binding.StatusBinding;

/**
 * <p></p >
 *
 * @author shenyue
 */

public class StatusBindingUtils {
    private StatusBindingUtils() {
    }

    public static Object parseStatusValue(StatusBinding statusBinding) {
        return AnnotationValueUtils.parseEnumValue(statusBinding, "statusClass", "statusValue");
    }
}
