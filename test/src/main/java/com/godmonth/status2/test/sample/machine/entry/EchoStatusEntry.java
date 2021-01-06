package com.godmonth.status2.test.sample.machine.entry;

import com.godmonth.status2.test.sample.domain.SampleModel;
import com.godmonth.status2.transitor.tx.intf.StatusEntry;
import com.godmonth.status2.transitor.tx.intf.TransitedResult;
import lombok.extern.slf4j.Slf4j;

/**
 * <p></p >
 *
 * @author shenyue
 */
@Slf4j
public class EchoStatusEntry implements StatusEntry<SampleModel, Void> {
    @Override
    public void nextStatusEntry(TransitedResult<SampleModel, Void> transitedResult) {
        log.debug("transitedResult:{}", transitedResult);
    }
}
