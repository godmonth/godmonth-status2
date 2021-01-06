package com.godmonth.status.test.sample.machine.advancer2;

import com.godmonth.status.advancer.intf.AdvanceRequest;
import com.godmonth.status.advancer.intf.AdvancedResult;
import com.godmonth.status.advancer.intf.StatusAdvancer2;
import com.godmonth.status.annotations.Advancer;
import com.godmonth.status.annotations.binding.ModelBinding;
import com.godmonth.status.test.sample.domain.SampleModel;
import com.godmonth.status.test.sample.domain.SampleStatus;
import com.godmonth.status.test.sample.machine.inst.SampleInstruction;
import com.godmonth.status.test.sample.machine.inst.SampleInstructionBinding;
import com.godmonth.status.test.sample.machine.trigger.SampleTrigger;
import com.godmonth.status.transitor.tx.intf.TriggerBehavior;
@Advancer
@ModelBinding(SampleModel.class)
@SampleStatusBinding(SampleStatus.PAID)
@SampleInstructionBinding(SampleInstruction.DELIVER)
public class DeliveryAdvancer implements StatusAdvancer2<SampleModel, SampleInstruction, SampleTrigger> {

    @Override
    public AdvancedResult<SampleModel, SampleTrigger> advance(AdvanceRequest<SampleModel, SampleInstruction> advanceRequest) {
        return new AdvancedResult<>(new TriggerBehavior<>(SampleTrigger.DELIVER));
    }
}
