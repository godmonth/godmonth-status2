package com.godmonth.status2.executor.impl;

import com.godmonth.status2.advancer.intf.AdvanceRequest;
import com.godmonth.status2.advancer.intf.AdvancedResult;
import com.godmonth.status2.advancer.intf.StatusAdvancer;
import com.godmonth.status2.advancer.intf.SyncData;
import com.godmonth.status2.analysis.intf.ModelAnalysis;
import com.godmonth.status2.executor.intf.ExecutionRequest;
import com.godmonth.status2.executor.intf.OrderExecutor;
import com.godmonth.status2.executor.intf.SyncResult;
import com.godmonth.status2.transitor.tx.intf.TxStatusTransitor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefaultOrderExecutor<MODEL, INST, TRIGGER> implements OrderExecutor<MODEL, INST> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultOrderExecutor.class);

    @Setter
    protected Function<Object, StatusAdvancer> advancerFunctions;

    @Setter
    protected TxStatusTransitor<MODEL, TRIGGER> txStatusTransitor;

    @Builder.Default
    @Setter
    protected ExecutorService executorService = Executors.newCachedThreadPool();

    @Setter
    protected ModelAnalysis<MODEL> modelAnalysis;

    public static Function<Object, StatusAdvancer> convert(List<Pair<Object, StatusAdvancer>> advancerBindings) {
        Map<Object, StatusAdvancer> advancerMap = new HashMap<>();
        for (Pair<Object, StatusAdvancer> advancerBinding : advancerBindings) {
            advancerMap.put(advancerBinding.getKey(), advancerBinding.getValue());
        }
        return advancerMap::get;
    }

    public void setAdvancerBindingMap(Map<Object, StatusAdvancer> advancerMap) {
        this.advancerFunctions = advancerMap::get;
    }

    public void setAdvancerBindingList(List<Pair<Object, StatusAdvancer>> advancerBindingList) {
        this.advancerFunctions = convert(advancerBindingList);
    }

    protected SyncResult<MODEL, ?> execute1(MODEL model, INST instruction, Object message) {
        modelAnalysis.validate(model);
        AdvancedResult<MODEL, TRIGGER> advancedResult = null;
        while (true) {
            Object status = modelAnalysis.getStatus(model);
            logger.trace("status:{}", status);

            StatusAdvancer<MODEL, INST, TRIGGER> advancer = null;
            if (instruction != null) {
                advancer = advancerFunctions.apply(Pair.of(status, instruction));
            }
            if (advancer == null) {
                advancer = advancerFunctions.apply(status);
            }
            logger.trace("advancer:{}", advancer);
            if (advancer == null) {
                break;
            }
            AdvanceRequest<MODEL, INST> advanceRequest = AdvanceRequest.<MODEL, INST>builder().model(model).instruction(instruction).message(message).build();
            advancedResult = advancer.advance(advanceRequest);
            logger.trace("advancedResult:{}", advancedResult);
            if (advancedResult == null) {
                break;
            }
            if (advancedResult.getTriggerBehavior() != null) {
                model = txStatusTransitor.transit(model, advancedResult.getTriggerBehavior());
                Validate.notNull(model, "transited model is null.");
                if (advancedResult.isDropInstruction()) {
                    instruction = null;
                    message = null;
                }
                switch (advancedResult.getNextOperation()) {
                    case ADVANCE:
                        advancedResult = null;
                        continue;
                    case ASYNC_ADVANCE:
                        logger.trace("executeAsync");
                        executeAsync1(model, instruction, message);
                        break;
                    case PAUSE:
                        logger.trace("pause");
                        break;
                }
            }
            break;
        }
        SyncData syncData = advancedResult != null ? advancedResult.getSyncData() : null;
        if (syncData == null) {
            return SyncResult.<MODEL, Object>builder().model(model).build();
        } else {
            return SyncResult.<MODEL, Object>builder().model(model).symbol(syncData.getSymbol()).value(syncData.getValue()).build();
        }
    }

    @Override
    public SyncResult<MODEL, ?> execute(ExecutionRequest<MODEL, INST> req) {
        return execute1(req.getModel(), req.getInstruction(), req.getMessage());
    }

    @Override
    public Future<SyncResult<MODEL, ?>> executeAsync(ExecutionRequest<MODEL, INST> req) {
        modelAnalysis.validate(req.getModel());
        return executeAsync1(req.getModel(), req.getInstruction(), req.getMessage());
    }

    protected Future<SyncResult<MODEL, ?>> executeAsync1(MODEL model, INST instruction, Object message) {
        return executorService.submit(() -> execute1(model, instruction, message));
    }

    public static class DefaultOrderExecutorBuilder<MODEL, INST, TRIGGER> {
        private Function<Object, StatusAdvancer> advancerFunctions;

        public DefaultOrderExecutorBuilder advancerBindingList(List<Pair<Object, StatusAdvancer>> advancerBindingList) {
            this.advancerFunctions = convert(advancerBindingList);
            return this;
        }

    }

}
