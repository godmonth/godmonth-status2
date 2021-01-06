package com.godmonth.status.analysis.impl.sm;

import com.godmonth.status.analysis.impl.Abc;
import com.godmonth.status.analysis.impl.model.AnnotationBeanModelAnalysis;
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
        AnnotationStateMachineAnalysis annotationStateMachineAnalysis = new AnnotationStateMachineAnalysis(annotationBeanModelAnalysis);
        System.out.println(annotationStateMachineAnalysis);
    }
}