package com.godmonth.status.analysis.impl.sm;

import com.godmonth.status.analysis.intf.ModelAnalysis;
import com.godmonth.status.annotations.Trigger;
import lombok.Builder;
import lombok.ToString;
import org.apache.commons.lang3.Validate;
import org.springframework.core.annotation.AnnotationUtils;


@ToString(callSuper = true)
public class AnnotationStateMachineAnalysis extends SimpleStateMachineAnalysis {

    @Builder(builderMethodName = "annoBuilder")
    public AnnotationStateMachineAnalysis(ModelAnalysis modelAnalysis) {
        super(modelAnalysis, getTrigger(modelAnalysis));
    }

    private static Class getTrigger(ModelAnalysis modelAnalysis) {
        Class statusClass = modelAnalysis.getStatusClass();
        Trigger trigger = AnnotationUtils.findAnnotation(statusClass, Trigger.class);
        Validate.notNull(trigger, "trigger is null");
        return trigger.value();
    }


}
