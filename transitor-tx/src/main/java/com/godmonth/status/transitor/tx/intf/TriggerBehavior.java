package com.godmonth.status.transitor.tx.intf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 跃迁参数
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TriggerBehavior<TRIGGER, MODEL> {

    private TRIGGER trigger;

    private TransitionCallback<MODEL, ?> transitionCallback;

    /**
     * @param trigger
     */
    public TriggerBehavior(TRIGGER trigger) {
        this.trigger = trigger;
    }

}
