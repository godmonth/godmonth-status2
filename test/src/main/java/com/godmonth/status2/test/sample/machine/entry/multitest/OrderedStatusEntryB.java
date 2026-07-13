package com.godmonth.status2.test.sample.machine.entry.multitest;

import com.godmonth.status2.annotations.Entry;
import com.godmonth.status2.test.sample.domain.SampleModel;
import com.godmonth.status2.test.sample.domain.SampleStatus;
import com.godmonth.status2.test.sample.machine.advancer.SampleStatusBinding;
import com.godmonth.status2.transitor.tx.intf.StatusEntry;
import com.godmonth.status2.transitor.tx.intf.TransitedResult;

/**
 * <p>order 较大的 Entry，用于多 Entry 顺序测试</p>
 *
 * @author shenyue
 */
@Entry(order = 20)
@SampleStatusBinding(SampleStatus.PAID)
public class OrderedStatusEntryB implements StatusEntry<SampleModel, Void> {

    @Override
    public void nextStatusEntry(TransitedResult<SampleModel, Void> transitedResult) {
        MultiEntryExecutionRecorder.EXECUTED_ORDERS.add(20);
    }
}
