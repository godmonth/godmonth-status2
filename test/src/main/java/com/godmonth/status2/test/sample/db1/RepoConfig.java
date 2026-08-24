package com.godmonth.status2.test.sample.db1;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("com.godmonth.status2.test.sample.domain")
@EnableJpaRepositories(basePackages = "com.godmonth.status2.test.sample.repo")
public class RepoConfig {

}
