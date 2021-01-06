package com.godmonth.status.transitor.core.impl;

import com.godmonth.status.test.sample.domain.SampleConfigMap;
import com.godmonth.status.test.sample.domain.SampleStatus;
import com.godmonth.status.test.sample.domain.SampleTrigger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SimpleStatusTransitorTest {
    private static SimpleStatusTransitor<SampleStatus, SampleTrigger> simpleStatusTransitor;

    @BeforeAll
    public static void prepare() throws IOException {
        simpleStatusTransitor = new SimpleStatusTransitor<>(SampleConfigMap.INSTANCE);
    }

    @Test
    public void transit() {
        SampleStatus transit = simpleStatusTransitor.transit(SampleStatus.CREATED, SampleTrigger.PAY);
        Assertions.assertEquals(transit, SampleStatus.PAID);
    }

}
