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
import com.google.common.util.concurrent.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

@Slf4j
@NoArgsConstructor
public class DefaultOrderExecutor<MODEL, INST, TRIGGER> implements OrderExecutor<MODEL, INST> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultOrderExecutor.class);

    protected Function<Object, StatusAdvancer> advancerRouter;
    @Getter
    @Setter
    protected TxStatusTransitor<MODEL, TRIGGER> txStatusTransitor;

    @Setter
    protected ExecutorService executorService = Executors.newCachedThreadPool();

    private ListeningExecutorService listeningExecutorService;
    private boolean innerBuild;

    @Setter
    protected ModelAnalysis<MODEL> modelAnalysis;

    @PostConstruct
    public void init() {
        if (executorService != null) {
            if (this.executorService instanceof ListeningExecutorService) {
                listeningExecutorService = (ListeningExecutorService) executorService;
            } else {
                listeningExecutorService = MoreExecutors.listeningDecorator(executorService);
                innerBuild = true;
            }
        }
    }

    @PreDestroy
    public void close() {
        if (listeningExecutorService != null && innerBuild) {
            listeningExecutorService.shutdown();
        }
    }

    @Builder
    public DefaultOrderExecutor(List<Pair<Object, StatusAdvancer>> advancerBindings, TxStatusTransitor<MODEL, TRIGGER> txStatusTransitor, ExecutorService executorService, ModelAnalysis<MODEL> modelAnalysis) {
        this(convert(advancerBindings), txStatusTransitor, executorService, modelAnalysis);
    }

    public DefaultOrderExecutor(Map<Object, StatusAdvancer> advancerRouterMap, TxStatusTransitor<MODEL, TRIGGER> txStatusTransitor, ExecutorService executorService, ModelAnalysis<MODEL> modelAnalysis) {
        this(advancerRouterMap::get, txStatusTransitor, executorService, modelAnalysis);
    }


    public DefaultOrderExecutor(Function<Object, StatusAdvancer> advancerRouter, TxStatusTransitor<MODEL, TRIGGER> txStatusTransitor, ExecutorService executorService, ModelAnalysis<MODEL> modelAnalysis) {
        this.advancerRouter = advancerRouter;
        logger.trace("advancerRouter:{}", advancerRouter);
        this.txStatusTransitor = txStatusTransitor;
        if (executorService != null) {
            this.executorService = executorService;
        }
        this.modelAnalysis = modelAnalysis;
        init();
    }


    public static Function<Object, StatusAdvancer> convert(List<Pair<Object, StatusAdvancer>> advancerBindings) {
        logger.trace("advancerBindingList:{}", advancerBindings);
        if (advancerBindings != null) {
            Map<Object, StatusAdvancer> advancerMap = new HashMap<>();
            for (Pair<Object, StatusAdvancer> advancerBinding : advancerBindings) {
                advancerMap.put(advancerBinding.getKey(), advancerBinding.getValue());
            }
            return advancerMap::get;
        } else {
            return null;
        }
    }

    public void setAdvancerRouterMap(Map<Object, StatusAdvancer> advancerRouterMap) {
        logger.trace("advancerRouterMap:{}", advancerRouterMap);
        if (advancerRouterMap != null) {
            this.advancerRouter = advancerRouterMap::get;
        } else {
            this.advancerRouter = null;
        }
    }


    public void setAdvancerRouterList(List<Pair<Object, StatusAdvancer>> advancerRouterList) {
        logger.trace("advancerBindingList:{}", advancerRouterList);
        this.advancerRouter = convert(advancerRouterList);
    }

    protected SyncResult<MODEL, ?> execute1(MODEL model, INST instruction, Object message) {
        logger.trace("execute1:{}", model);
        modelAnalysis.validate(model);
        AdvancedResult<MODEL, TRIGGER> advancedResult = null;
        while (true) {
            Object status = modelAnalysis.getStatus(model);
            logger.trace("status:{}", status);

            StatusAdvancer<MODEL, INST, TRIGGER> advancer = null;
            if (instruction != null) {
                advancer = advancerRouter.apply(Pair.of(status, instruction));
            }
            if (advancer == null) {
                advancer = advancerRouter.apply(status);
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
                if (advancedResult.getUpdatedModel() != null) {
                    model = advancedResult.getUpdatedModel();
                }
                model = txStatusTransitor.transit(model, advancedResult.getTriggerBehavior());
                Validate.notNull(model, "transited model is null.");
                if (advancedResult.getNextInstruction() != null) {
                    if (advancedResult.getNextInstruction().isDrop()) {
                        instruction = null;
                        message = null;
                    }

                    if (advancedResult.getNextInstruction().getInstruction() != null) {
                        instruction = (INST) advancedResult.getNextInstruction().getInstruction();
                        message = advancedResult.getNextInstruction().getMessage();
                    }
                }
                switch (advancedResult.getNextOperation()) {
                    case ADVANCE:
                        advancedResult = null;
                        continue;
                    case ASYNC_ADVANCE:
                        logger.trace("executeAsync");
                        innerAsnnc(model, instruction, message);
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
        return executeAsync2(req);
    }

    @Override
    public ListenableFuture<SyncResult<MODEL, ?>> executeAsync2(ExecutionRequest<MODEL, INST> executionRequest) {
        modelAnalysis.validate(executionRequest.getModel());
        return innerAsnnc(executionRequest.getModel(), executionRequest.getInstruction(), executionRequest.getMessage());
    }

    private FutureCallback<SyncResult<MODEL, ?>> futureCallback = new FutureCallback<SyncResult<MODEL, ?>>() {
        @Override
        public void onSuccess(@Nullable SyncResult<MODEL, ?> result) {
        }

        @Override
        public void onFailure(Throwable t) {
            log.error("", t);
        }
    };

    protected ListenableFuture<SyncResult<MODEL, ?>> innerAsnnc(MODEL model, INST instruction, Object message) {
        ListenableFuture<SyncResult<MODEL, ?>> future = listeningExecutorService.submit(() -> execute1(model, instruction, message));
        Futures.addCallback(future, futureCallback, executorService);
        return future;
    }


}
