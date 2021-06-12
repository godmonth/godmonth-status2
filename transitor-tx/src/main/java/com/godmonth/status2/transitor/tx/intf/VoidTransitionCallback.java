package com.godmonth.status2.transitor.tx.intf;

/**
 * 事务内
 *
 * @param <MODEL>
 * @author shenyue
 */
public interface VoidTransitionCallback<MODEL> extends TransitionCallback<MODEL, Void> {
    @Override
    default Void beforeMerge(MODEL model) {
        beforeMergeReturnNothing(model);
        return null;
    }

    void beforeMergeReturnNothing(MODEL model);
}
