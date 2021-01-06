package com.godmonth.status.builder.advancer;

import com.godmonth.status.advancer.intf.AdvanceRequest;
import com.godmonth.status.advancer.intf.AdvancedResult;
import com.godmonth.status.advancer.intf.StatusAdvancer2;
import com.godmonth.status.annotations.Advancer;
import com.godmonth.status.annotations.binding.ModelBinding;
import com.godmonth.status.annotations.binding.StatusBinding;
import com.godmonth.status.builder.domain.SampleModel;
import com.godmonth.status.builder.domain.SampleStatus;

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