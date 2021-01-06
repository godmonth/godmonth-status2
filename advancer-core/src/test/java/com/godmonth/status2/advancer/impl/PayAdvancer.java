package com.godmonth.status2.advancer.impl;

import com.godmonth.status2.advancer.intf.AdvancedResult;
import com.godmonth.status2.test.sample.domain.SampleModel;
import com.godmonth.status2.test.sample.domain.SampleTrigger;
import com.godmonth.status2.transitor.tx.intf.TriggerBehavior;

public class PayAdvancer extends AbstractAdvancer<SampleModel, Void, SampleTrigger> {

    @Override
    public AdvancedResult<SampleModel, SampleTrigger> advance(SampleModel model, Void instruction, Object message)
            throws IllegalStateException {
        System.out.println("advanced");
        return new AdvancedResult<>(new TriggerBehavior<>(SampleTrigger.PAY));
    }

}
