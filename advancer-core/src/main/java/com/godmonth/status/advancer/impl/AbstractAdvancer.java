package com.godmonth.status.advancer.impl;

import com.godmonth.status.advancer.intf.AdvanceRequest;
import com.godmonth.status.advancer.intf.AdvancedResult;
import com.godmonth.status.advancer.intf.StatusAdvancer;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @param <MODEL>
 * @param <INST>
 * @param <TRIGGER>
 * @deprecated use {@link StatusAdvancer}
 */
@Deprecated
public abstract class AbstractAdvancer<MODEL, INST, TRIGGER> implements StatusAdvancer<MODEL, INST, TRIGGER> {
    @Setter
    protected Object availableStatus;
    @Setter
    protected INST expectedInstruction;

    @Override
    public AdvancedResult<MODEL, TRIGGER> advance(AdvanceRequest<MODEL, INST> advanceRequest) {
        return advance(advanceRequest.getModel(), advanceRequest.getInstruction(), advanceRequest.getMessage());
    }

    protected abstract AdvancedResult<MODEL, TRIGGER> advance(MODEL model, INST instruction, Object message);

    @Override
    public Object getKey() {
        if (expectedInstruction != null) {
            return Pair.of(availableStatus, expectedInstruction);
        } else {
            return availableStatus;
        }
    }


}
