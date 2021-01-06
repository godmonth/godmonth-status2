package com.godmonth.status2.test.sample.machine.advancer;

import com.godmonth.status2.advancer.impl.AbstractAdvancer;
import com.godmonth.status2.advancer.intf.AdvancedResult;
import com.godmonth.status2.test.sample.machine.trigger.SampleTrigger;
import com.godmonth.status2.test.sample.domain.SampleModel;
import com.godmonth.status2.test.sample.domain.SampleStatus;

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
