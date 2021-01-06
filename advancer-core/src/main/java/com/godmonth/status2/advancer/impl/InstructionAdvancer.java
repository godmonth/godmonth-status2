package com.godmonth.status2.advancer.impl;

import com.godmonth.status2.advancer.intf.AdvancedResult;
import com.godmonth.status2.advancer.intf.StatusAdvancer2;
import org.apache.commons.lang3.Validate;

/**
 * @param <MODEL>
 * @param <INST>
 * @param <TRIGGER>
 * @deprecated use {@link StatusAdvancer2}
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
