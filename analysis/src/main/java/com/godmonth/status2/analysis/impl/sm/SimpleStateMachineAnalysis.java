package com.godmonth.status2.analysis.impl.sm;

import com.godmonth.status2.analysis.intf.ModelAnalysis;
import com.godmonth.status2.analysis.intf.StateMachineAnalysis;
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
