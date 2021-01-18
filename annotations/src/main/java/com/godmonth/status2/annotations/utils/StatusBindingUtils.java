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

    public static Object[] parseStatusValues(StatusBinding statusBinding) {
        return AnnotationValueUtils.parseArrayEnumValue(statusBinding, "statusClass", "statusValue");
    }

}
