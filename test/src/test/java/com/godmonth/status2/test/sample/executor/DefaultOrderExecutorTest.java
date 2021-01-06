package com.godmonth.status2.test.sample.executor;

import com.godmonth.status2.advancer.intf.AdvancedResult;
import com.godmonth.status2.advancer.intf.StatusAdvancer;
import com.godmonth.status2.analysis.impl.model.SimpleBeanModelAnalysis;
import com.godmonth.status2.analysis.impl.model.TypeFieldPredicate;
import com.godmonth.status2.executor.impl.DefaultOrderExecutor;
import com.godmonth.status2.executor.intf.ExecutionRequest;
import com.godmonth.status2.executor.intf.OrderExecutor;
import com.godmonth.status2.executor.intf.SyncResult;
import com.godmonth.status2.test.sample.domain.SampleModel;
import com.godmonth.status2.test.sample.domain.SampleStatus;
import com.godmonth.status2.test.sample.machine.trigger.SampleTrigger;
import com.godmonth.status2.transitor.core.impl.SimpleStatusTransitor;
import com.godmonth.status2.transitor.tx.impl.TxStatusTransitorImpl;
import com.godmonth.status2.transitor.tx.intf.StatusEntry;
import com.godmonth.status2.transitor.tx.intf.TransitedResult;
import com.godmonth.status2.transitor.tx.intf.TriggerBehavior;
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

    private static OrderExecutor<SampleModel, Object> orderExecutor;

    @BeforeAll
    public static void prepare() {
        TypeFieldPredicate typeFieldPredicate = TypeFieldPredicate.builder().propertyName("type").expectedValue("test").build();
        SimpleBeanModelAnalysis<SampleModel> analysis = new SimpleBeanModelAnalysis<>(SampleModel.class, "status", Arrays.asList(typeFieldPredicate));
        DefaultOrderExecutor<SampleModel, Object, SampleTrigger> defaultOrderExecutor = new DefaultOrderExecutor<>();
        defaultOrderExecutor.setModelAnalysis(analysis);
        Map<Object, StatusAdvancer> advancerMap = new HashMap<>();
        advancerMap.put(SampleStatus.CREATED, advanceRequest -> new AdvancedResult<>(new TriggerBehavior<>(SampleTrigger.PAY)));
        defaultOrderExecutor.setAdvancerBindingMap(advancerMap);

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
    }

    private static void print(TransitedResult transitedResult) {
        logger.debug("transitedResult:{}", transitedResult);
    }

    @Test
    public void execute() {
        SampleModel sampleModel = new SampleModel();
        sampleModel.setStatus(SampleStatus.CREATED);
        sampleModel.setType("test");
        SyncResult<SampleModel, ?> execute = orderExecutor.execute(ExecutionRequest.<SampleModel, Object>builder().model(sampleModel).instruction("eee").message("fff").build());
        Assertions.assertEquals(execute.getModel().getStatus(), SampleStatus.PAID);
    }


}
