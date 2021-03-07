package com.godmonth.status2.analysis.impl;

import com.godmonth.status2.analysis.impl.AbcStatus;
import com.godmonth.status2.analysis.impl.AbcTrigger;
import com.godmonth.status2.analysis.impl.Def;
import com.godmonth.status2.annotations.Status;

/**
 * <p></p >
 *
 * @author shenyue
 */
public class DefChild extends Def {
    @Override
    @Status(triggerClass = AbcTrigger.class)
    public AbcStatus getStatus() {
        return super.getStatus();
    }
}
