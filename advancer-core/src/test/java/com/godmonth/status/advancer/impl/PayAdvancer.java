package com.godmonth.status.advancer.impl;

import com.godmonth.status.advancer.intf.AdvancedResult;
import com.godmonth.status.test.sample.domain.SampleModel;
import com.godmonth.status.test.sample.domain.SampleTrigger;
import com.godmonth.status.transitor.tx.intf.TriggerBehavior;

public class PayAdvancer extends AbstractAdvancer<SampleModel, Void, SampleTrigger> {

    @Override
    public AdvancedResult<SampleModel, SampleTrigger> advance(SampleModel model, Void instruction, Object message)
            throws IllegalStateException {
        System.out.println("advanced");
        return new AdvancedResult<>(new TriggerBehavior<>(SampleTrigger.PAY));
    }

}
