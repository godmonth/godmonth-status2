package com.godmonth.status.transitor.core.impl;

import com.godmonth.status.transitor.core.intf.StatusTransitor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.function.Function;

@Builder
@AllArgsConstructor
public class SimpleStatusTransitor<STATUS, TRIGGER> implements StatusTransitor<STATUS, TRIGGER> {
    private final Function<STATUS, Function<TRIGGER, STATUS>> config;

    @Override
    public STATUS transit(STATUS status, TRIGGER trigger) {
        Function<TRIGGER, STATUS> triggerBehavior = config.apply(status);
        if (triggerBehavior == null) {
            throw new IllegalStateException("status not found:" + status);
        }
        STATUS nextStatus = triggerBehavior.apply(trigger);
        if (nextStatus == null) {
            throw new IllegalArgumentException("status:" + status + ", trigger not found:" + trigger);
        }
        return nextStatus;
    }

}
