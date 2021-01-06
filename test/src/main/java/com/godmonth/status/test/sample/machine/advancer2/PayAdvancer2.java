package com.godmonth.status.test.sample.machine.advancer2;

import com.godmonth.status.advancer.intf.AdvanceRequest;
import com.godmonth.status.advancer.intf.AdvancedResult;
import com.godmonth.status.advancer.intf.NextOperation;
import com.godmonth.status.advancer.intf.StatusAdvancer2;
import com.godmonth.status.advancer.intf.SyncData;
import com.godmonth.status.annotations.Advancer;
import com.godmonth.status.annotations.binding.ModelBinding;
import com.godmonth.status.annotations.binding.StatusBinding;
import com.godmonth.status.test.sample.domain.SampleModel;
import com.godmonth.status.test.sample.domain.SampleStatus;
import com.godmonth.status.test.sample.machine.trigger.SampleTrigger;
import com.godmonth.status.test.sample.repo.SampleModelRepository;
import com.godmonth.status.transitor.tx.intf.TriggerBehavior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Advancer
@ModelBinding(SampleModel.class)
@StatusBinding(statusClass = SampleStatus.class, statusValue = "CREATED")
public class PayAdvancer2 implements StatusAdvancer2<SampleModel, String, SampleTrigger> {
    @Autowired
    private SampleModelRepository sampleModelRepository;

    @Value("${aaa.bbb}")
    private String value;

    @Override
    public AdvancedResult<SampleModel, SampleTrigger> advance(AdvanceRequest<SampleModel, String> advanceRequest)
            throws IllegalStateException {
        System.out.println(value);
        if ("pay".equals(advanceRequest.getInstruction()) && "balance".equals(advanceRequest.getMessage())) {
            final SyncData syncData = SyncData.builder().symbol("sss").value("vvvv").build();
            return AdvancedResult.<SampleModel, SampleTrigger>builder().triggerBehavior(new TriggerBehavior<>(SampleTrigger.PAY)).syncData(syncData).nextOperation(NextOperation.PAUSE).build();
        }
        return null;

    }

}
