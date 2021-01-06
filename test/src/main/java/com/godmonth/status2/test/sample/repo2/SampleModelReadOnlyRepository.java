package com.godmonth.status2.test.sample.repo2;

import com.godmonth.status2.test.sample.domain.SampleModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleModelReadOnlyRepository extends CrudRepository<SampleModel, Long> {


}
