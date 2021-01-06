package com.godmonth.status.builder.transitor;

import com.godmonth.status.analysis.intf.StateMachineAnalysis;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

import java.util.function.Function;

@Setter
public class JsonDefinitionFactoryBean<STATUS, TRIGGER> implements FactoryBean<Function<STATUS, Function<TRIGGER, STATUS>>> {
    private StateMachineAnalysis stateMachineAnalysis;
    private Resource resource;


    @Override
    public Function<STATUS, Function<TRIGGER, STATUS>> getObject() throws Exception {
        return JsonDefinitionBuilder.<STATUS, TRIGGER>builder().statusClass(stateMachineAnalysis.getModelAnalysis().getStatusClass()).triggerClass(stateMachineAnalysis.getTriggerClass()).resource(resource).build();
    }

    @Override
    public Class<?> getObjectType() {
        return Function.class;
    }
}
