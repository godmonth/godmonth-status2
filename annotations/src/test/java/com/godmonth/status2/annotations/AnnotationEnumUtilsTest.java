package com.godmonth.status2.annotations;

import com.godmonth.status2.annotations.binding.StatusBinding;
import com.godmonth.status2.annotations.utils.AnnotationValueUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <p></p >
 *
 * @author shenyue
 */

class AnnotationEnumUtilsTest {
    @Test
    void t1() {
        StatusBinding annotation = T1.class.getAnnotation(StatusBinding.class);
        Object o = AnnotationValueUtils.parseArrayEnumValue(annotation, "statusClass", "statusValue");
        Assertions.assertArrayEquals(new String[]{"VV"}, (Object[]) o);
    }

    @Test
    void t2() {
        StatusBinding annotation = T2.class.getAnnotation(StatusBinding.class);
        Object o = AnnotationValueUtils.parseArrayEnumValue(annotation, "statusClass", "statusValue");
        Assertions.assertArrayEquals(new SampleStatus[]{SampleStatus.S1}, (Object[]) o);
    }

    public static enum SampleStatus {
        S1
    }

    @StatusBinding(statusValue = "VV")
    public static class T1 {

    }

    @StatusBinding(statusClass = SampleStatus.class, statusValue = "S1")
    public static class T2 {

    }

}