package com.godmonth.status2.analysis.impl.sm;

import com.godmonth.status2.analysis.impl.Abc;
import com.godmonth.status2.analysis.impl.AnnotationBeanModelAnalysis;
import org.junit.jupiter.api.Test;

/**
 * <p></p >
 *
 * @author shenyue
 */
class AnnotationStateMachineAnalysisTest {
    @Test
    void name() {
        AnnotationBeanModelAnalysis annotationBeanModelAnalysis = new AnnotationBeanModelAnalysis(Abc.class);
        System.out.println(annotationBeanModelAnalysis);
    }
}