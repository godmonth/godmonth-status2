package com.godmonth.status.test.sample.machine.advancer;

import com.godmonth.status.advancer.impl.AbstractAdvancer;
import com.godmonth.status.advancer.intf.AdvancedResult;
import com.godmonth.status.test.sample.machine.trigger.SampleTrigger;
import com.godmonth.status.test.sample.domain.SampleModel;
import com.godmonth.status.test.sample.domain.SampleStatus;

public class CheckAdvancer extends AbstractAdvancer<SampleModel, String, SampleTrigger> {
    {
        availableStatus = SampleStatus.PAID;
    }

    @Override
    public AdvancedResult<SampleModel, SampleTrigger> advance(SampleModel model, String instruction, Object message)
            throws IllegalStateException {
        return null;
    }

}
