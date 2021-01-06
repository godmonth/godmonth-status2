package com.godmonth.status.advancer.impl;

import com.godmonth.status.advancer.intf.AdvancedResult;
import org.apache.commons.lang3.Validate;

/**
 * @param <MODEL>
 * @param <INST>
 * @param <TRIGGER>
 * @deprecated use {@link com.godmonth.status.advancer.intf.StatusAdvancer2}
 */
@Deprecated
public abstract class InstructionAdvancer<MODEL, INST, TRIGGER> extends AbstractAdvancer<MODEL, INST, TRIGGER> {

    @Override
    final public AdvancedResult<MODEL, TRIGGER> advance(MODEL model, INST instruction, Object message) {
        Validate.notNull(instruction, "instruction is null");
        Validate.isTrue(expectedInstruction == instruction, "expected instruction not equals input instruction.");
        return doAdvance(model, message);
    }

    protected abstract AdvancedResult<MODEL, TRIGGER> doAdvance(MODEL model, Object message);

}
