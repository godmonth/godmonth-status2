package com.godmonth.status2.test.sample.repo;

import com.godmonth.status2.test.sample.domain.SampleModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleModelRepository extends CrudRepository<SampleModel, Long> {


}
