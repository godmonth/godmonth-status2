package com.godmonth.status.transitor.tx.intf;

/**
 * 状态跃迁器
 *
 * @param <MODEL>
 * @param <TRIGGER>
 * @author shenyue
 */
public interface TxStatusTransitor<MODEL, TRIGGER> {

    MODEL transit(MODEL model, TriggerBehavior<TRIGGER, MODEL> triggerBehavior);

}
