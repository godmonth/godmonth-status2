package com.godmonth.status2.test.sample.machine.cfg2up1;

import com.godmonth.status2.executor.intf.OrderExecutor;
import com.godmonth.status2.test.sample.db1.RepoConfig;
import com.godmonth.status2.test.sample.domain.SampleModel;
import com.godmonth.status2.test.sample.machine.cfg2.Sample2Test;
import com.godmonth.status2.test.sample.repo.SampleModelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

/**
 * <p></p >
 *
 * @author shenyue
 */
@ComponentScan
@AutoConfigureDataJpa
@SpringBootTest(classes = {RepoConfig.class, OEConfig.class})
@EnableAutoConfiguration
public class Sample2Up1Test {
    @Autowired
    private OrderExecutor<SampleModel, Object> sampleModelOrderExecutor;

    @Autowired
    private SampleModelRepository sampleModelRepository;

    @Test
    void name() {
        Sample2Test.test(sampleModelRepository, sampleModelOrderExecutor);
    }
}
