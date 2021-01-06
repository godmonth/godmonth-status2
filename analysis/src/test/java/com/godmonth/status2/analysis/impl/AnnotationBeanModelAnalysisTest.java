package com.godmonth.status2.analysis.impl;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;


/**
 * <p></p >
 *
 * @author shenyue
 */
public class AnnotationBeanModelAnalysisTest {
    @Test
    void name() {
        AnnotationBeanModelAnalysis<Abc> objectAnnotationBeanModelAnalysis = new AnnotationBeanModelAnalysis<>(Abc.class);
        System.out.println(objectAnnotationBeanModelAnalysis);
    }

    @Test
    void name2() {
        Field abc = FieldUtils.getField(Abc.class, "abc", true);
        System.out.println(abc);
    }
}