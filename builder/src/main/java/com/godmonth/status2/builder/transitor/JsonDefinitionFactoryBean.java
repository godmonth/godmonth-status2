package com.godmonth.status2.builder.transitor;

import com.godmonth.status2.analysis.intf.ModelAnalysis;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

import java.util.function.Function;

@Setter
public class JsonDefinitionFactoryBean<STATUS, TRIGGER> implements FactoryBean<Function<STATUS, Function<TRIGGER, STATUS>>> {
    private ModelAnalysis modelAnalysis;
    private Resource resource;


    @Override
    public Function<STATUS, Function<TRIGGER, STATUS>> getObject() throws Exception {
        return JsonDefinitionBuilder.<STATUS, TRIGGER>builder().statusClass(modelAnalysis.getStatusClass()).triggerClass(modelAnalysis.getTriggerClass()).resource(resource).build();
    }

    @Override
    public Class<?> getObjectType() {
        return Function.class;
    }
}
