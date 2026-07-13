package com.godmonth.status2.test.sample.executor;

import com.godmonth.status2.advancer.intf.AdvancedResult;
import com.godmonth.status2.advancer.intf.StatusAdvancer;
import com.godmonth.status2.analysis.impl.SimpleBeanModelAnalysis;
import com.godmonth.status2.analysis.intf.ModelAnalysis;
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
import com.godmonth.status2.transitor.tx.intf.TriggerBehavior;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>多 Entry 顺序执行与异常中断手动装配测试</p>
 *
 * @author shenyue
 */
public class MultiEntryOrderTest {

    private List<String> executedEntries;

    @BeforeEach
    public void prepare() {
        executedEntries = new ArrayList<>();
    }

    @Test
    public void multipleEntriesExecutedInOrder() {
        OrderExecutor<SampleModel, Object> executor = createExecutor(false);

        SampleModel sampleModel = new SampleModel();
        sampleModel.setStatus(SampleStatus.CREATED);
        sampleModel.setType("test");

        SyncResult<SampleModel, ?> execute = executor.execute(ExecutionRequest.<SampleModel, Object>builder().model(sampleModel).instruction("eee").message("fff").build());
        Assertions.assertEquals(SampleStatus.PAID, execute.getModel().getStatus());

        Assertions.assertEquals(2, executedEntries.size());
        Assertions.assertEquals("EntryA", executedEntries.get(0));
        Assertions.assertEquals("EntryB", executedEntries.get(1));
    }

    @Test
    public void failingEntryAbortsLaterEntries() {
        OrderExecutor<SampleModel, Object> executor = createExecutor(true);

        SampleModel sampleModel = new SampleModel();
        sampleModel.setStatus(SampleStatus.CREATED);
        sampleModel.setType("test");

        Assertions.assertThrows(RuntimeException.class, () ->
                executor.execute(ExecutionRequest.<SampleModel, Object>builder().model(sampleModel).instruction("eee").message("fff").build()));

        // EntryA(order=10) 先执行，中间匿名 Entry 抛异常，EntryB(order=20) 不应执行
        Assertions.assertEquals(2, executedEntries.size());
        Assertions.assertEquals("EntryA", executedEntries.get(0));
        Assertions.assertEquals("FailingEntry", executedEntries.get(1));
    }

    private OrderExecutor<SampleModel, Object> createExecutor(boolean includeFailing) {
        ModelAnalysis<SampleModel> modelAnalysis = SimpleBeanModelAnalysis.<SampleModel>builder()
                .modelClass(SampleModel.class)
                .statusPropertyName("status")
                .statusClass(SampleStatus.class)
                .triggerClass(SampleTrigger.class)
                .build();

        Map<Object, StatusAdvancer> advancerMap = new HashMap<>();
        advancerMap.put(SampleStatus.CREATED, advanceRequest -> new AdvancedResult<>(new TriggerBehavior<>(SampleTrigger.PAY)));

        SimpleStatusTransitor<SampleStatus, SampleTrigger> statusTransitor = new SimpleStatusTransitor<>(SampleConfigMap.INSTANCE);

        TxStatusTransitorImpl<SampleModel, SampleStatus, SampleTrigger> txStatusTransitor = new TxStatusTransitorImpl<>();
        txStatusTransitor.setStatusTransitor(statusTransitor);
        txStatusTransitor.setModelAnalysis(modelAnalysis);
        txStatusTransitor.setModelMerger(sampleModel -> sampleModel);
        txStatusTransitor.setTransactionOperations(TransactionOperations.withoutTransaction());

        Map<SampleStatus, List<StatusEntry<SampleModel, Object>>> entryMap = new HashMap<>();
        List<StatusEntry<SampleModel, Object>> paidEntries = new ArrayList<>();
        paidEntries.add(transitedResult -> executedEntries.add("EntryA"));
        if (includeFailing) {
            paidEntries.add(transitedResult -> {
                executedEntries.add("FailingEntry");
                throw new RuntimeException("expected failure");
            });
        }
        paidEntries.add(transitedResult -> executedEntries.add("EntryB"));
        entryMap.put(SampleStatus.PAID, paidEntries);

        txStatusTransitor.setStatusEntryListFunction(entryMap::get);

        return new DefaultOrderExecutor<>(advancerMap, txStatusTransitor, null, modelAnalysis);
    }
}
