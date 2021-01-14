package com.godmonth.status2.test.sample.machine.cfg3up1;

import com.godmonth.status2.executor.intf.OrderExecutor;
import com.godmonth.status2.test.sample.db2.RepoConfig2;
import com.godmonth.status2.test.sample.db3.RepoConfig3;
import com.godmonth.status2.test.sample.domain.SampleModel;
import com.godmonth.status2.test.sample.machine.cfg2.Sample2Test;
import com.godmonth.status2.test.sample.repo.SampleModelRepository;
import com.godmonth.status2.test.sample.repo2.SampleModelReadOnlyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * <p></p >
 *
 * @author shenyue
 */
@ComponentScan
@SpringBootTest(classes = {RepoConfig2.class, RepoConfig3.class, OEConfig2.class})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class Sample3Up1Test {
    @Autowired
    private OrderExecutor<SampleModel, Object> sampleModelOrderExecutor;

    @Autowired
    private SampleModelRepository sampleModelRepository;

    @Autowired
    private SampleModelReadOnlyRepository sampleModelReadOnlyRepository;

    @Qualifier("d3")
    @Autowired
    private DataSource dataSource3;

    @Test
    void name() throws SQLException {
        Sample2Test.test(sampleModelRepository, sampleModelOrderExecutor);
        {
            //查看数据源3
            Connection connection3 = dataSource3.getConnection();
            ResultSet resultSet3 = connection3.createStatement().executeQuery("select count(*) from sample_model");
            resultSet3.next();
            Assertions.assertEquals(resultSet3.getInt(1), 0);
        }
        //查看数据源2
        Optional<SampleModel> byId = sampleModelRepository.findById(1L);
        Assertions.assertTrue(byId.isPresent());
        Optional<SampleModel> byId2 = sampleModelReadOnlyRepository.findById(1L);
        Assertions.assertFalse(byId2.isPresent());
    }
}
