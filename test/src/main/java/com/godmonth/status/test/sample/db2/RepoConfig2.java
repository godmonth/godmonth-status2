package com.godmonth.status.test.sample.db2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})

@EnableJpaRepositories(entityManagerFactoryRef = "e2", transactionManagerRef = "tm2", basePackages = "com.godmonth.status.test.sample.repo")
//@EntityScan("com.godmonth.status.test.sample.domain")
public class RepoConfig2 {
    @Bean("d2")
    @ConfigurationProperties(prefix = "spring.db2")
    public DataSource userDataSource() {
        return DataSourceBuilder.create().build();
    }

    // userEntityManager bean
    @Bean("e2")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("d2") DataSource userDataSource, JpaProperties jpaProperties) {
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(vendorAdapter, jpaProperties.getProperties(), null);
        HibernateProperties hibernateProperties = new HibernateProperties();
        Map<String, Object> properties = hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
        return builder.dataSource(userDataSource).properties(properties).packages("com.godmonth.status.test.sample.domain").build();
    }

    @Bean("tm2")
    public PlatformTransactionManager userTransactionManager(@Qualifier("e2") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean("tt2")
    public TransactionOperations transactionTemplate(@Qualifier("tm2") PlatformTransactionManager platformTransactionManager) {
        return new TransactionTemplate(platformTransactionManager);
    }

}
