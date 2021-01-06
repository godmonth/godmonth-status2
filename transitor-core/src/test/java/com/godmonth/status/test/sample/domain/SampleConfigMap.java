package com.godmonth.status.test.sample.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SampleConfigMap {
    public static final Function<SampleStatus, Function<SampleTrigger, SampleStatus>> INSTANCE;

    static {
        Map<SampleTrigger, SampleStatus> payTriggerConfig = new HashMap<>();
        payTriggerConfig.put(SampleTrigger.PAY, SampleStatus.PAID);

        Map<SampleStatus, Function<SampleTrigger, SampleStatus>> map = new HashMap<SampleStatus, Function<SampleTrigger, SampleStatus>>();
        map.put(SampleStatus.CREATED, payTriggerConfig::get);
        INSTANCE = map::get;
    }
}
