package com.godmonth.status2.advancer.impl;

import com.godmonth.status2.advancer.intf.AdvanceRequest;
import com.godmonth.status2.advancer.intf.AdvancedResult;
import com.godmonth.status2.advancer.intf.StatusAdvancer;
import com.godmonth.status2.test.sample.domain.SampleModel;
import com.godmonth.status2.test.sample.domain.SampleTrigger;
import com.godmonth.status2.transitor.tx.intf.TriggerBehavior;

public class PayAdvancer implements StatusAdvancer<SampleModel, Void, SampleTrigger> {
    @Override
    public AdvancedResult<SampleModel, SampleTrigger> advance(AdvanceRequest<SampleModel, Void> advanceRequest) {
        System.out.println("advanced");
        return new AdvancedResult<>(new TriggerBehavior<>(SampleTrigger.PAY));
    }

}
