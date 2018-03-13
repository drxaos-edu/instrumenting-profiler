package com.example.demo.domain;

import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.hibernate.dialect.MySQL57InnoDBDialect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        basePackages = {"com.example.demo.domain.main"}
)
public class MainDbConfig {

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "proxyDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource proxyDataSource() {
        return ProxyDataSourceBuilder
                .create(dataSource())
//TODO
//                .beforeQuery((execInfo, queryInfoList) -> {
//                    Profiler.startCall("DB", QueryUtil.queryToCall(queryInfoList));
//                })
//                .afterQuery((execInfo, queryInfoList) -> {
//                    Profiler.endCall("DB", QueryUtil.queryToCall(queryInfoList));
//                })
                .build();
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("proxyDataSource") DataSource proxyDataSource
    ) {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
        properties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
        properties.put("hibernate.dialect", MySQL57InnoDBDialect.class.getName());
        return builder
                .dataSource(proxyDataSource)
                .packages("com.example.demo.domain.main")
                .persistenceUnit("main")
                .properties(properties)
                .build();
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
