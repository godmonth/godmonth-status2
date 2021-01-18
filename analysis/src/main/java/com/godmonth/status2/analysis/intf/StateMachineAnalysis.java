package com.godmonth.status2.analysis.intf;

import com.godmonth.status2.analysis.impl.AnnotationField;
import com.godmonth.status2.analysis.impl.BindingKeyUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.function.Function;

/**
 * @param <MODEL>
 */
@ToString
@Getter
public class StateMachineAnalysis<MODEL> {
    protected Function<Class, Object[]> bindingKeyFunction;

    private ModelAnalysis<MODEL> modelAnalysis;

    @Builder
    public StateMachineAnalysis(ModelAnalysis<MODEL> modelAnalysis, AnnotationField statusBindingField, AnnotationField instBindingField) {
        this.modelAnalysis = modelAnalysis;
        bindingKeyFunction = componentClass -> BindingKeyUtils.getBindingKey(componentClass, statusBindingField, instBindingField);
    }


}
