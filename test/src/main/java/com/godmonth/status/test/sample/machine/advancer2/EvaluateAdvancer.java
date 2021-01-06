package com.godmonth.status.test.sample.machine.advancer2;

import com.godmonth.status.advancer.intf.AdvanceRequest;
import com.godmonth.status.advancer.intf.AdvancedResult;
import com.godmonth.status.advancer.intf.StatusAdvancer2;
import com.godmonth.status.annotations.Advancer;
import com.godmonth.status.annotations.binding.InstructionBinding;
import com.godmonth.status.annotations.binding.ModelBinding;
import com.godmonth.status.test.sample.domain.SampleModel;
import com.godmonth.status.test.sample.domain.SampleStatus;
import com.godmonth.status.test.sample.machine.inst.SampleInstruction;
import com.godmonth.status.test.sample.machine.trigger.SampleTrigger;
import com.godmonth.status.transitor.tx.intf.TriggerBehavior;

@Advancer
@ModelBinding(SampleModel.class)
@SampleStatusBinding(SampleStatus.DELIVERED)
@InstructionBinding(instructionClass = SampleInstruction.class, instructionValue = "EVALUATE")
public class EvaluateAdvancer implements StatusAdvancer2<SampleModel, SampleInstruction, SampleTrigger> {

    @Override
    public AdvancedResult<SampleModel, SampleTrigger> advance(AdvanceRequest<SampleModel, SampleInstruction> advanceRequest) {
        return new AdvancedResult<>(new TriggerBehavior<>(SampleTrigger.EVALUATE));
    }
}
