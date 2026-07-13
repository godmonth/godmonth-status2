package com.godmonth.status2.test.sample.machine.entry.multitest;

import com.godmonth.status2.executor.intf.ExecutionRequest;
import com.godmonth.status2.executor.intf.OrderExecutor;
import com.godmonth.status2.executor.intf.SyncResult;
import com.godmonth.status2.test.sample.db1.RepoConfig;
import com.godmonth.status2.test.sample.domain.SampleModel;
import com.godmonth.status2.test.sample.domain.SampleStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.data.jpa.test.autoconfigure.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

/**
 * <p>多 Entry 排序 Spring 集成测试</p>
 *
 * @author shenyue
 */
@ComponentScan
@AutoConfigureDataJpa
@SpringBootTest(classes = {RepoConfig.class, MultiEntryTestConfig.class})
@EnableAutoConfiguration
public class MultiEntrySpringTest {

    @Autowired
    private OrderExecutor<SampleModel, Object> multiEntryTestOrderExecutor;

    @BeforeEach
    public void prepare() {
        MultiEntryExecutionRecorder.EXECUTED_ORDERS.clear();
    }

    @Test
    public void entriesExecutedInOrder() {
        SampleModel sampleModel = new SampleModel();
        sampleModel.setStatus(SampleStatus.CREATED);
        sampleModel.setType("test");
        ExecutionRequest<SampleModel, Object> req = ExecutionRequest.<SampleModel, Object>builder().model(sampleModel).instruction("pay").message("balance").build();
        SyncResult<SampleModel, ?> execute = multiEntryTestOrderExecutor.execute(req);
        Assertions.assertEquals(SampleStatus.PAID, execute.getModel().getStatus());
        Assertions.assertEquals(List.of(10, 20), MultiEntryExecutionRecorder.EXECUTED_ORDERS);
    }
}
