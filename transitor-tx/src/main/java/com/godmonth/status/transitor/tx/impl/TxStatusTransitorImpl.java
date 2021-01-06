package com.godmonth.status.transitor.tx.impl;

import com.godmonth.status.analysis.intf.ModelAnalysis;
import com.godmonth.status.transitor.core.intf.StatusTransitor;
import com.godmonth.status.transitor.tx.intf.StatusEntry;
import com.godmonth.status.transitor.tx.intf.TransitedResult;
import com.godmonth.status.transitor.tx.intf.TriggerBehavior;
import com.godmonth.status.transitor.tx.intf.TxStatusTransitor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionOperations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class TxStatusTransitorImpl<MODEL, STATUS, TRIGGER>
        implements TxStatusTransitor<MODEL, TRIGGER> {

    protected ModelAnalysis<MODEL> modelAnalysis;

    protected StatusTransitor<STATUS, TRIGGER> statusTransitor;

    protected Function<STATUS, StatusEntry<MODEL, Object>> statusEntryFunction;

    protected TransactionOperations transactionOperations;

    protected Merger<MODEL> modelMerger;

    public static <STATUS, MODEL> Function<STATUS, StatusEntry<MODEL, Object>> convert(List<Pair<STATUS, StatusEntry>> entryBindList) {
        Map<STATUS, StatusEntry<MODEL, Object>> statusStatusEntryMap = new HashMap<>();
        for (Pair<STATUS, StatusEntry> binding : entryBindList) {
            statusStatusEntryMap.put(binding.getKey(), binding.getValue());
        }
        return statusStatusEntryMap::get;
    }

    public void setStatusEntryBindList(List<Pair<STATUS, StatusEntry>> entryBindList) {
        setStatusEntryFunction(convert(entryBindList));
    }

    @Override
    public MODEL transit(MODEL model, TriggerBehavior<TRIGGER, MODEL> triggerBehavior) {
        STATUS nextStatus = beforeChange(model, triggerBehavior.getTrigger());
        TransitedResult<MODEL, Object> transitedResult = transactionOperations.execute((TransactionStatus status) -> {
            modelAnalysis.setStatus(model, nextStatus);
            Object accessory = null;
            if (triggerBehavior.getTransitionCallback() != null) {
                accessory = triggerBehavior.getTransitionCallback().beforeMerge(model);
            }
            MODEL mergedModelInTx = modelMerger.mergeInTx(model);
            return new TransitedResult(mergedModelInTx, accessory);
        });
        afterChange(transitedResult);

        return transitedResult.getModel();
    }

    protected STATUS beforeChange(MODEL model, TRIGGER trigger) {
        STATUS status = modelAnalysis.getStatus(model);
        Validate.notNull(status, "status is null");

        STATUS nextStatus = statusTransitor.transit(status, trigger);
        Validate.notNull(nextStatus, "nextStatus is null");

        return nextStatus;
    }

    protected void afterChange(TransitedResult<MODEL, Object> transitedResult) {
        STATUS status = modelAnalysis.getStatus(transitedResult.getModel());
        Validate.notNull(status, "status is null");
        if (statusEntryFunction != null) {
            StatusEntry<MODEL, Object> statusEntry = statusEntryFunction.apply(status);
            if (statusEntry != null) {
                statusEntry.nextStatusEntry(transitedResult);
            }
        }
    }

    public static class TxStatusTransitorImplBuilder<MODEL, STATUS, TRIGGER> {
        protected Function<STATUS, StatusEntry<MODEL, Object>> statusEntryFunction;

        public TxStatusTransitorImplBuilder statusEntryBindList(List<Pair<STATUS, StatusEntry>> entryBindList) {
            this.statusEntryFunction = convert(entryBindList);
            return this;
        }

    }
}
