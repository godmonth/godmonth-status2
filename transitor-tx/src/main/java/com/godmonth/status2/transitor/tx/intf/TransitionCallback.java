package com.godmonth.status2.transitor.tx.intf;

/**
 * 事务内
 *
 * @param <MODEL>
 * @author shenyue
 */
public interface TransitionCallback<MODEL, ACCESSORY> {
    ACCESSORY beforeMerge(MODEL model);
}
