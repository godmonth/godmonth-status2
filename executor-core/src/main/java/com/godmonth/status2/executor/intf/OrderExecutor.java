package com.godmonth.status2.executor.intf;


import com.godmonth.status2.transitor.tx.intf.TxStatusTransitor;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Future;

/**
 * 订单执行
 *
 * @param <MODEL>
 * @param <INST>
 * @author shenyue
 */
public interface OrderExecutor<MODEL, INST> {
    public TxStatusTransitor getTxStatusTransitor();

    SyncResult<MODEL, ?> execute(ExecutionRequest<MODEL, INST> executionRequest);

    @Deprecated
    Future<SyncResult<MODEL, ?>> executeAsync(ExecutionRequest<MODEL, INST> executionRequest);

    /**
     * 默认已经增加了打印异常日志的功能.调用者如果使用自己的callback。不要忘记自己补充打印异常日志的功能。
     *
     * @param executionRequest
     * @return
     */
    ListenableFuture<SyncResult<MODEL, ?>> executeAsync2(ExecutionRequest<MODEL, INST> executionRequest);

}
