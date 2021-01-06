package com.godmonth.status.test.sample.machine.advancer;

import com.godmonth.status.advancer.impl.AbstractAdvancer;
import com.godmonth.status.advancer.intf.AdvancedResult;
import com.godmonth.status.test.sample.machine.trigger.SampleTrigger;
import com.godmonth.status.test.sample.domain.SampleModel;
import com.godmonth.status.test.sample.domain.SampleStatus;
import com.godmonth.status.transitor.tx.intf.TransitionCallback;
import com.godmonth.status.transitor.tx.intf.TriggerBehavior;

public class PayAdvancer extends AbstractAdvancer<SampleModel, String, SampleTrigger> {
    {
        availableStatus = SampleStatus.CREATED;
    }

    @Override
    public AdvancedResult<SampleModel, SampleTrigger> advance(SampleModel model, String instruction, Object message)
            throws IllegalStateException {
        System.out.println("advanced");
        if ("pay".equals(instruction) && "balance".equals(message)) {
            return new AdvancedResult<>(new TriggerBehavior<>(SampleTrigger.PAY, new TransitionCallback<SampleModel, Object>() {
                @Override
                public Object beforeMerge(SampleModel sampleModel) {
                    return "hello3333";
                }
            }));
        }
        return null;

    }

}
