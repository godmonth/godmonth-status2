package com.godmonth.status.test.sample.repotest;

import com.godmonth.status.test.sample.domain.SampleModel;
import com.godmonth.status.test.sample.domain.SampleStatus;
import com.godmonth.status.test.sample.db1.RepoConfig;
import com.godmonth.status.test.sample.repo.SampleModelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

/**
 * <p>
 * </p >
 *
 * @author shenyue
 */
@AutoConfigureDataJpa
@SpringBootTest(classes = {RepoConfig.class})
@EnableAutoConfiguration
class SampleModelRepositoryTest {
    @Autowired
    private SampleModelRepository sampleModelRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    public void save() {
        SampleModel sampleModel = new SampleModel();
        sampleModel.setStatus(SampleStatus.CREATED);
        sampleModel.setType("test");
        SampleModel sampleModel1 = sampleModelRepository.save(sampleModel);
        System.out.println(sampleModel1);
        Optional<SampleModel> byId = sampleModelRepository.findById(1L);
        System.out.println(byId.get());
    }

}