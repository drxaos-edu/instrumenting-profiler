package com.example.demo.domain;

import com.example.demo.domain.air.PostgresqlDialect;
import com.example.demo.profiler.Profiler;
import com.example.demo.profiler.util.RecordUtil;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        entityManagerFactoryRef = "airEntityManagerFactory",
        transactionManagerRef = "airTransactionManager",
        basePackages = {"com.example.demo.domain.air"}
)
public class AirDbConfig {

    @Bean(name = "airDataSource")
    @ConfigurationProperties(prefix = "air.datasource")
    public DataSource airDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "proxyAirDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource proxyAirDataSource() {
        return ProxyDataSourceBuilder
                .create(airDataSource())
                .beforeQuery((execInfo, queryInfoList) -> {
                    Profiler.startCall("DB", RecordUtil.queryToCall(queryInfoList));
                })
                .afterQuery((execInfo, queryInfoList) -> {
                    Profiler.endCall("DB", RecordUtil.queryToCall(queryInfoList));
                })
                .build();
    }

    @Bean(name = "airEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    airEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("proxyAirDataSource") DataSource proxyAirDataSource
    ) {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
        properties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
        properties.put("hibernate.dialect", PostgresqlDialect.class.getName());
        return
                builder
                        .dataSource(proxyAirDataSource)
                        .packages("com.example.demo.domain.air")
                        .persistenceUnit("air")
                        .properties(properties)
                        .build();
    }

    @Bean(name = "airTransactionManager")
    public PlatformTransactionManager airTransactionManager(
            @Qualifier("airEntityManagerFactory") EntityManagerFactory airEntityManagerFactory
    ) {
        return new JpaTransactionManager(airEntityManagerFactory);
    }
}