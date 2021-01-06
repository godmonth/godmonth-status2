package com.godmonth.status.test.sample.repo2;

import com.godmonth.status.test.sample.domain.SampleModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleModelReadOnlyRepository extends CrudRepository<SampleModel, Long> {


}
