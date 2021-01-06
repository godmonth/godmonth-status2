package com.godmonth.status.advancer.impl;

import com.godmonth.status.advancer.intf.AdvancedResult;
import com.godmonth.status.advancer.intf.NextOperation;
import com.godmonth.status.transitor.tx.intf.TriggerBehavior;
import lombok.Setter;

/**
 * @author shenyue
 * @deprecated use {@link com.godmonth.status.advancer.intf.StatusAdvancer2}
 */
@Deprecated
public class DoNothingAdvancer<MODEL, INST, TRIGGER> extends InstructionAdvancer<MODEL, INST, TRIGGER> {
    @Setter
    protected TRIGGER trigger;
    @Setter
    protected NextOperation nextOperation = NextOperation.ADVANCE;

    @Override
    protected AdvancedResult<MODEL, TRIGGER> doAdvance(MODEL model, Object message) {
        return new AdvancedResult<>(new TriggerBehavior<TRIGGER, MODEL>(trigger), nextOperation);
    }

}
