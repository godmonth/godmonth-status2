package com.godmonth.status.executor.impl;

import com.godmonth.status.advancer.intf.SyncResult;
import com.godmonth.status.executor.intf.OrderExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;


public class OldDefaultOrderExecutor<MODEL, INST, TRIGGER> extends DefaultOrderExecutor<MODEL, INST, TRIGGER> implements OrderExecutor<MODEL, INST> {


    @Override
    public SyncResult<MODEL, ?> execute(MODEL model, INST instruction, Object param) {
        final com.godmonth.status.executor.intf.SyncResult<MODEL, ?> modelSyncResult = execute1(model, instruction, param);
        SyncResult<MODEL, Object> syncResult = new SyncResult<MODEL, Object>();
        syncResult.setModel(modelSyncResult.getModel());
        syncResult.setSymbol(modelSyncResult.getSymbol());
        syncResult.setValue(modelSyncResult.getValue());
        return syncResult;
    }


    @Override
    public Future<SyncResult<MODEL, ?>> executeAsync(MODEL model, INST instruction, Object message) {
        modelAnalysis.validate(model);
        return executorService.submit(new Callable<SyncResult<MODEL, ?>>() {

            @Override
            public SyncResult<MODEL, ?> call() {
                return execute(model, instruction, message);
            }
        });
    }
}
