package com.godmonth.status2.test.sample.machine.cfg2;

import com.godmonth.status2.executor.intf.ExecutionRequest;
import com.godmonth.status2.executor.intf.OrderExecutor;
import com.godmonth.status2.executor.intf.SyncResult;
import com.godmonth.status2.test.sample.db1.RepoConfig;
import com.godmonth.status2.test.sample.domain.SampleModel;
import com.godmonth.status2.test.sample.domain.SampleStatus;
import com.godmonth.status2.test.sample.machine.inst.SampleInstruction;
import com.godmonth.status2.test.sample.repo.SampleModelRepository;
import org.junit.jupiter.api.Assertions;
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
@SpringBootTest(classes = {RepoConfig.class, SampleOrderExecutorConfig2.class})
@EnableAutoConfiguration
public class Sample2Test {

    @Autowired
    private OrderExecutor<SampleModel, Object> sampleModelOrderExecutor;

    @Autowired
    private SampleModelRepository sampleModelRepository;

    @Test
    void name() {
        SampleModel sampleModel = new SampleModel();
        sampleModel.setStatus(SampleStatus.CREATED);
        sampleModel.setType("test");
        sampleModel = sampleModelRepository.save(sampleModel);
        {
            ExecutionRequest<SampleModel, Object> req = ExecutionRequest.<SampleModel, Object>builder().model(sampleModel).instruction("pay").message("balance").build();
            SyncResult<SampleModel, ?> execute = sampleModelOrderExecutor.execute(req);
            System.out.println(execute);
            Assertions.assertEquals(SampleStatus.PAID, execute.getModel().getStatus());
            sampleModel = execute.getModel();
        }
        {
            ExecutionRequest<SampleModel, Object> req = ExecutionRequest.<SampleModel, Object>builder().model(sampleModel).instruction(SampleInstruction.DELIVER).message("yunda").build();
            SyncResult<SampleModel, ?> execute = sampleModelOrderExecutor.execute(req);
            System.out.println(execute);
            Assertions.assertEquals(SampleStatus.DELIVERED, execute.getModel().getStatus());
            sampleModel = execute.getModel();
        }
        {
            ExecutionRequest<SampleModel, Object> req = ExecutionRequest.<SampleModel, Object>builder().model(sampleModel).instruction(SampleInstruction.EVALUATE).message("5").build();
            SyncResult<SampleModel, ?> execute = sampleModelOrderExecutor.execute(req);
            System.out.println(execute);
            Assertions.assertEquals(SampleStatus.EVALUATED, execute.getModel().getStatus());
        }
    }
}
