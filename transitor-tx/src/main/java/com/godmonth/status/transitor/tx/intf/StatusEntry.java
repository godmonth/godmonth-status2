package com.godmonth.status.transitor.tx.intf;

/**
 * 状态进入
 *
 * @author shenyue
 */
public interface StatusEntry<MODEL, ACCESSORY> {

    void nextStatusEntry(TransitedResult<MODEL, ACCESSORY> transitedResult);

}
