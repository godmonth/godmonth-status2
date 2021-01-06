package com.godmonth.status2.advancer.impl2;

import com.godmonth.status2.advancer.intf.AdvanceRequest;
import com.godmonth.status2.advancer.intf.AdvancedResult;
import com.godmonth.status2.advancer.intf.StatusAdvancer2;
import com.godmonth.status2.annotations.binding.InstructionBinding;
import lombok.Setter;
import org.apache.commons.lang3.Validate;

/**
 * 这种情况不用 {@link InstructionBinding}
 */
public abstract class InstructionAdvancer2<MODEL, INST, TRIGGER> implements StatusAdvancer2<MODEL, INST, TRIGGER> {

    @Setter
    protected INST expectedInstruction;

    @Override
    public AdvancedResult<MODEL, TRIGGER> advance(AdvanceRequest<MODEL, INST> advanceRequest) {
        Validate.notNull(advanceRequest.getInstruction(), "instruction is null");
        Validate.isTrue(expectedInstruction == advanceRequest.getInstruction(), "expected instruction not equals input instruction.");
        return doAdvance(advanceRequest.getModel(), advanceRequest.getMessage());
    }

    protected abstract AdvancedResult<MODEL, TRIGGER> doAdvance(MODEL model, Object message);

}
