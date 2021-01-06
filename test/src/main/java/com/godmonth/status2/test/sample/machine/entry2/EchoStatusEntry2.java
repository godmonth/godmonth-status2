package com.godmonth.status2.test.sample.machine.entry2;

import com.godmonth.status2.annotations.Entry;
import com.godmonth.status2.test.sample.domain.SampleModel;
import com.godmonth.status2.test.sample.domain.SampleStatus;
import com.godmonth.status2.test.sample.machine.advancer2.SampleStatusBinding;
import com.godmonth.status2.transitor.tx.intf.StatusEntry;
import com.godmonth.status2.transitor.tx.intf.TransitedResult;
import lombok.extern.slf4j.Slf4j;

/**
 * <p></p >
 *
 * @author shenyue
 */
@Slf4j
@Entry
@SampleStatusBinding(SampleStatus.PAID)
public class EchoStatusEntry2 implements StatusEntry<SampleModel, Void> {
    @Override
    public void nextStatusEntry(TransitedResult<SampleModel, Void> transitedResult) {
        log.debug("transitedResult:{}", transitedResult);
    }
}
