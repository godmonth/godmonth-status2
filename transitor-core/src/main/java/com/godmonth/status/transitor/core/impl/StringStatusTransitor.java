package com.godmonth.status.transitor.core.impl;

import com.godmonth.status.transitor.core.intf.StatusTransitor;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class StringStatusTransitor<STATUS, TRIGGER> implements StatusTransitor<STATUS, TRIGGER> {

    private final Map<String, Map<String, String>> config;
    private Class<STATUS> statusClass;

    @Override
    public STATUS transit(STATUS status, TRIGGER trigger) {
        Map<String, String> triggerBehavior = config.get(status.toString());
        if (triggerBehavior == null) {
            throw new IllegalStateException("status not found:" + status);
        }
        String nextStatus = triggerBehavior.get(trigger.toString());

        if (nextStatus == null) {
            throw new IllegalArgumentException("status:" + status + ", trigger not found:" + trigger);
        }
        if (statusClass.equals(String.class)) {
            return (STATUS) nextStatus;
        } else if (statusClass.isEnum()) {
            Class<? extends Enum> enumClass = (Class<? extends Enum>) statusClass;
            Enum anEnum = Enum.valueOf(enumClass, nextStatus);
            return (STATUS) anEnum;
        } else {
            throw new IllegalArgumentException();
        }
    }

}
