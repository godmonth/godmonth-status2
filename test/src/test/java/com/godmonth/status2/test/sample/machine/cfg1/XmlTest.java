package com.godmonth.status2.test.sample.machine.cfg1;

import com.godmonth.status2.executor.intf.ExecutionRequest;
import com.godmonth.status2.executor.intf.OrderExecutor;
import com.godmonth.status2.executor.intf.SyncResult;
import com.godmonth.status2.test.sample.db1.RepoConfig;
import com.godmonth.status2.test.sample.domain.SampleModel;
import com.godmonth.status2.test.sample.domain.SampleStatus;
import com.godmonth.status2.test.sample.repo.SampleModelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;

import javax.annotation.Resource;

/**
 * <p></p >
 *
 * @author shenyue
 */
@AutoConfigureDataJpa
@SpringBootTest(classes = {RepoConfig.class})
@ImportResource(locations = {"classpath:/sample-bean.xml"})
@EnableAutoConfiguration
public class XmlTest {
    @Autowired
    private SampleModelRepository sampleModelRepository;

    @Resource(name = "sampleModelExecutor")
    private OrderExecutor<SampleModel, Object> sampleModelExecutor;

    @Test
    void name() {
        SampleModel sampleModel = new SampleModel();
        sampleModel.setStatus(SampleStatus.CREATED);
        sampleModel.setType("test");
        sampleModel = sampleModelRepository.save(sampleModel);
        ExecutionRequest<SampleModel, Object> req = ExecutionRequest.<SampleModel, Object>builder().model(sampleModel).instruction("pay").message("balance").build();
        SyncResult<SampleModel, ?> execute = sampleModelExecutor.execute(req);
        System.out.println(execute);
        Assertions.assertEquals(SampleStatus.PAID, execute.getModel().getStatus());
    }
}
