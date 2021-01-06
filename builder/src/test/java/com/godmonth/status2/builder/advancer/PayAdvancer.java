package com.godmonth.status2.builder.advancer;

import com.godmonth.status2.advancer.intf.AdvanceRequest;
import com.godmonth.status2.advancer.intf.AdvancedResult;
import com.godmonth.status2.advancer.intf.StatusAdvancer2;
import com.godmonth.status2.annotations.Advancer;
import com.godmonth.status2.annotations.binding.ModelBinding;
import com.godmonth.status2.annotations.binding.StatusBinding;
import com.godmonth.status2.builder.domain.SampleModel;
import com.godmonth.status2.builder.domain.SampleStatus;

/**
 * <p></p >
 *
 * @author shenyue
 */
@Advancer
@ModelBinding(SampleModel.class)
@StatusBinding(statusClass = SampleStatus.class, statusValue = "CREATED")
public class PayAdvancer implements StatusAdvancer2 {

    @Override
    public AdvancedResult advance(AdvanceRequest advanceRequest) {
        return null;
    }
}