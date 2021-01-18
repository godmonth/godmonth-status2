package com.godmonth.status2.test.sample.machine.advancer;

import com.godmonth.status2.test.sample.domain.SampleModel;
import com.godmonth.status2.test.sample.machine.inst.SampleInstruction;
import com.godmonth.status2.test.sample.machine.inst.SampleInstructionBinding;
import com.godmonth.status2.test.sample.machine.trigger.SampleTrigger;
import com.godmonth.status2.advancer.intf.AdvanceRequest;
import com.godmonth.status2.advancer.intf.AdvancedResult;
import com.godmonth.status2.advancer.intf.StatusAdvancer;
import com.godmonth.status2.annotations.Advancer;
import com.godmonth.status2.annotations.binding.ModelBinding;
import com.godmonth.status2.test.sample.domain.SampleStatus;
import com.godmonth.status2.transitor.tx.intf.TriggerBehavior;

@Advancer
@ModelBinding(SampleModel.class)
@SampleStatusBinding(SampleStatus.PAID)
@SampleInstructionBinding(SampleInstruction.DELIVER)
public class DeliveryAdvancer implements StatusAdvancer<SampleModel, SampleInstruction, SampleTrigger> {

    @Override
    public AdvancedResult<SampleModel, SampleTrigger> advance(AdvanceRequest<SampleModel, SampleInstruction> advanceRequest) {
        return new AdvancedResult<>(new TriggerBehavior<>(SampleTrigger.DELIVER));
    }
}
