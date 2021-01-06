package com.godmonth.status2.annotations.utils;

import com.godmonth.status2.annotations.binding.StatusBinding;

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
