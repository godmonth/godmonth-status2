package com.godmonth.status.analysis.impl.sm;

import com.godmonth.status.analysis.intf.ModelAnalysis;
import com.godmonth.status.analysis.intf.StateMachineAnalysis;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class SimpleStateMachineAnalysis implements StateMachineAnalysis {

    protected ModelAnalysis modelAnalysis;

    protected Class triggerClass;

}
