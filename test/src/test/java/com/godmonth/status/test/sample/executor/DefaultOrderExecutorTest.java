package com.godmonth.status.test.sample.executor;

import com.godmonth.status.advancer.intf.StatusAdvancer;
import com.godmonth.status.analysis.impl.model.SimpleBeanModelAnalysis;
import com.godmonth.status.analysis.impl.model.TypeFieldPredicate;
import com.godmonth.status.executor.impl.OldDefaultOrderExecutor;
import com.godmonth.status.executor.intf.ExecutionRequest;
import com.godmonth.status.executor.intf.OrderExecutor;
import com.godmonth.status.executor.intf.OrderExecutor2;
import com.godmonth.status.executor.intf.SyncResult;
import com.godmonth.status.test.sample.domain.SampleModel;
import com.godmonth.status.test.sample.domain.SampleStatus;
import com.godmonth.status.test.sample.machine.advancer.CheckAdvancer;
import com.godmonth.status.test.sample.machine.advancer.PayAdvancer;
import com.godmonth.status.test.sample.machine.trigger.SampleTrigger;
import com.godmonth.status.transitor.core.impl.SimpleStatusTransitor;
import com.godmonth.status.transitor.tx.impl.TxStatusTransitorImpl;
import com.godmonth.status.transitor.tx.intf.StatusEntry;
import com.godmonth.status.transitor.tx.intf.TransitedResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionOperations;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class DefaultOrderExecutorTest {

    private static final Logger logger = LoggerFactory.getLogger(DefaultOrderExecutorTest.class);

    private static OrderExecutor<SampleModel, String> orderExecutor;
    private static OrderExecutor2<SampleModel, String> orderExecutor2;

    @BeforeAll
    public static void prepare() {
        TypeFieldPredicate typeFieldPredicate = TypeFieldPredicate.builder().propertyName("type").expectedValue("test").build();
        SimpleBeanModelAnalysis<SampleModel> analysis = new SimpleBeanModelAnalysis<>(SampleModel.class, "status", Arrays.asList(typeFieldPredicate));
        OldDefaultOrderExecutor<SampleModel, String, SampleTrigger> defaultOrderExecutor = new OldDefaultOrderExecutor<>();
        defaultOrderExecutor.setModelAnalysis(analysis);
        Map<SampleStatus, StatusAdvancer<SampleModel, String, SampleTrigger>> advancers = new HashMap<>();
        advancers.put(SampleStatus.CREATED, new PayAdvancer());
        advancers.put(SampleStatus.PAID, new CheckAdvancer());
        defaultOrderExecutor.setAdvancerFunctions(advancers::get);

        TxStatusTransitorImpl<SampleModel, SampleStatus, SampleTrigger> txStatusTransitor = new TxStatusTransitorImpl<>();
        SimpleStatusTransitor<SampleStatus, SampleTrigger> statusTransitor = new SimpleStatusTransitor<>(
                SampleConfigMap.INSTANCE);

        txStatusTransitor.setStatusTransitor(statusTransitor);
        txStatusTransitor.setModelAnalysis(analysis);
        Map<SampleStatus, StatusEntry> sampleStatusStatusEntryMap = Collections.singletonMap(SampleStatus.PAID, DefaultOrderExecutorTest::print);
        txStatusTransitor.setStatusEntryFunction(sampleStatusStatusEntryMap::get);
        txStatusTransitor.setModelMerger(sampleModel -> sampleModel);
        txStatusTransitor.setTransactionOperations(TransactionOperations.withoutTransaction());
        defaultOrderExecutor.setTxStatusTransitor(txStatusTransitor);

        orderExecutor = defaultOrderExecutor;
        orderExecutor2 = defaultOrderExecutor;
    }

    private static void print(TransitedResult transitedResult) {
        logger.debug("transitedResult:{}", transitedResult);
    }

    @Test
    public void execute() {
        SampleModel sampleModel = new SampleModel();
        sampleModel.setStatus(SampleStatus.CREATED);
        sampleModel.setType("test");
        final com.godmonth.status.advancer.intf.SyncResult<SampleModel, ?> execute = orderExecutor.execute(sampleModel, "eee", "fff");
        Assertions.assertEquals(execute.getModel().getStatus(), SampleStatus.PAID);
    }

    @Test
    public void execute2() {
        SampleModel sampleModel = new SampleModel();
        sampleModel.setStatus(SampleStatus.CREATED);
        sampleModel.setType("test");
        final ExecutionRequest<SampleModel, String> request = ExecutionRequest.<SampleModel, String>builder().model(sampleModel).instruction("eee").message("fff").build();
        final SyncResult<SampleModel, ?> execute = orderExecutor2.execute(request);
        Assertions.assertEquals(execute.getModel().getStatus(), SampleStatus.CREATED);
    }
}
