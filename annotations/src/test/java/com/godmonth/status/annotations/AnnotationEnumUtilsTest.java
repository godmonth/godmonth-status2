package com.godmonth.status.annotations;

import com.godmonth.status.annotations.binding.StatusBinding;
import com.godmonth.status.annotations.utils.AnnotationValueUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * <p></p >
 *
 * @author shenyue
 */

class AnnotationEnumUtilsTest {
    @Test
    void t1() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        StatusBinding annotation = T1.class.getAnnotation(StatusBinding.class);
        Object o = AnnotationValueUtils.parseEnumValue(annotation, "statusClass", "statusValue");
        Assertions.assertEquals("VV", o);
    }

    @Test
    void t2() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        StatusBinding annotation = T2.class.getAnnotation(StatusBinding.class);
        Object o = AnnotationValueUtils.parseEnumValue(annotation, "statusClass", "statusValue");
        Assertions.assertEquals(SampleStatus.S1, o);
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