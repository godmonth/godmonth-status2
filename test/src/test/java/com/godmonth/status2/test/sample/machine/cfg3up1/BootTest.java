package com.godmonth.status2.test.sample.machine.cfg3up1;

import com.godmonth.status2.test.sample.db2.RepoConfig2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

/**
 * <p></p >
 *
 * @author shenyue
 */
@ComponentScan
@SpringBootTest(classes = {RepoConfig2.class, OEConfig2.class})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class BootTest {

    @Test
    void name() {
        System.out.println("ok");
    }
}
