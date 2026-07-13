package com.godmonth.status2.transitor.tx.impl;

import com.godmonth.status2.analysis.intf.ModelAnalysis;
import com.godmonth.status2.transitor.core.intf.StatusTransitor;
import com.godmonth.status2.transitor.tx.intf.StatusEntry;
import com.godmonth.status2.transitor.tx.intf.TransitedResult;
import com.godmonth.status2.transitor.tx.intf.TriggerBehavior;
import com.godmonth.status2.transitor.tx.intf.TxStatusTransitor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionOperations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
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

    protected Function<STATUS, List<StatusEntry<MODEL, Object>>> statusEntryListFunction;

    protected TransactionOperations transactionOperations;

    protected Merger<MODEL> modelMerger;

    public static <STATUS, MODEL> Function<STATUS, List<StatusEntry<MODEL, Object>>> convert(List<Pair<STATUS, StatusEntry>> entryBindList) {
        Map<STATUS, List<StatusEntry<MODEL, Object>>> statusStatusEntryMap = new LinkedHashMap<>();
        for (Pair<STATUS, StatusEntry> binding : entryBindList) {
            statusStatusEntryMap.computeIfAbsent(binding.getKey(), k -> new ArrayList<>()).add(binding.getValue());
        }
        return statusStatusEntryMap::get;
    }

    public void setStatusEntryListFunction(Function<STATUS, List<StatusEntry<MODEL, Object>>> statusEntryListFunction) {
        this.statusEntryListFunction = statusEntryListFunction;
    }

    public void setStatusEntryBindingList(List<Pair<STATUS, StatusEntry>> entryBindingList) {
        setStatusEntryListFunction(convert(entryBindingList));
    }

    public void setStatusEntryBindingMap(Map<STATUS, StatusEntry> entryBindingMap) {
        setStatusEntryListFunction(status -> {
            StatusEntry<MODEL, Object> entry = entryBindingMap.get(status);
            return entry != null ? Collections.singletonList(entry) : null;
        });
    }

    public void setStatusEntryFunction(Function<STATUS, StatusEntry<MODEL, Object>> statusEntryFunction) {
        setStatusEntryListFunction(status -> {
            StatusEntry<MODEL, Object> entry = statusEntryFunction.apply(status);
            return entry != null ? Collections.singletonList(entry) : null;
        });
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
        if (statusEntryListFunction != null) {
            List<StatusEntry<MODEL, Object>> statusEntries = statusEntryListFunction.apply(status);
            if (statusEntries != null) {
                for (StatusEntry<MODEL, Object> statusEntry : statusEntries) {
                    statusEntry.nextStatusEntry(transitedResult);
                }
            }
        }
    }

    public static class TxStatusTransitorImplBuilder<MODEL, STATUS, TRIGGER> {
        protected Function<STATUS, List<StatusEntry<MODEL, Object>>> statusEntryListFunction;

        public TxStatusTransitorImplBuilder statusEntryBindList(List<Pair<STATUS, StatusEntry>> entryBindList) {
            this.statusEntryListFunction = convert(entryBindList);
            return this;
        }

    }
}
