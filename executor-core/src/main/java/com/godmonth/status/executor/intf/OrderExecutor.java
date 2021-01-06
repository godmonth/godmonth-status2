package com.godmonth.status.executor.intf;

import com.godmonth.status.advancer.intf.SyncResult;

import java.util.concurrent.Future;

/**
 * 使用@{@link OrderExecutor2}
 *
 * @param <MODEL>
 * @param <INST>
 * @author shenyue
 */
@Deprecated
public interface OrderExecutor<MODEL, INST> {
    SyncResult<MODEL, ?> execute(MODEL model, INST instruction, Object param);

    Future<SyncResult<MODEL, ?>> executeAsync(MODEL model, INST instruction, Object param);
}
