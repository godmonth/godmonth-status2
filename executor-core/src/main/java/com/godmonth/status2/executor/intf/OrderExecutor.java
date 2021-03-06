package com.godmonth.status2.executor.intf;


import java.util.concurrent.Future;

/**
 * 订单执行
 *
 * @param <MODEL>
 * @param <INST>
 * @author shenyue
 */
public interface OrderExecutor<MODEL, INST> {
    SyncResult<MODEL, ?> execute(ExecutionRequest<MODEL, INST> executionRequest);

    Future<SyncResult<MODEL, ?>> executeAsync(ExecutionRequest<MODEL, INST> executionRequest);
}
