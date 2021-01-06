package com.godmonth.status.builder.transitor;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Builder;
import org.apache.commons.lang3.Validate;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


public class JsonDefinitionBuilder {


    @Builder
    private static <STATUS, TRIGGER> Function<STATUS, Function<TRIGGER, STATUS>> build(Class<STATUS> statusClass, Class<TRIGGER> triggerClass, Resource resource) throws IOException {
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        JavaType javaType = typeFactory.constructParametricType(StatusMachineDefinition.class, statusClass, triggerClass);
        CollectionType collectionType = typeFactory.constructCollectionType(List.class, javaType);
        List<StatusMachineDefinition> statusDefinitions = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = resource.getInputStream()) {
            statusDefinitions = objectMapper.readValue(inputStream, collectionType);
        }

        Map<STATUS, Function<TRIGGER, STATUS>> statusConfigs = new HashMap<>();

        for (StatusMachineDefinition<STATUS, TRIGGER> statusDefinition : statusDefinitions) {
            Validate.notNull(statusDefinition.getStatus(), "status is null");
            Validate.notNull(statusDefinition.getTriggers(), "triggerDefinitions is null");
            Map<TRIGGER, STATUS> triggerConfig = new HashMap<>();
            for (TriggerDefinition<TRIGGER, STATUS> triggerDefinition : statusDefinition.getTriggers()) {
                Validate.notNull(triggerDefinition.getTrigger(), "trigger is null");
                Validate.notNull(triggerDefinition.getNextStatus(), "nextStatus is null");
                triggerConfig.put(triggerDefinition.getTrigger(), triggerDefinition.getNextStatus());
            }
            statusConfigs.put(statusDefinition.getStatus(), triggerConfig::get);
        }
        return statusConfigs::get;
    }


}
