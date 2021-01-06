package com.godmonth.status2.advancer.intf;

/**
 * @param <MODEL> MODEL
 * @author shenyue
 */
public interface StatusAdvancer<MODEL, INST, TRIGGER> {
    AdvancedResult<MODEL, TRIGGER> advance(AdvanceRequest<MODEL, INST> advanceRequest);
}
