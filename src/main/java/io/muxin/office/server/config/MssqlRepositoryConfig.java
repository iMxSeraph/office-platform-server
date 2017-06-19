package io.muxin.office.server.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

/**
 * MSSQL 数据源配置
 * Created by muxin on 2017/2/23.
 */
@Configuration
@EnableJpaRepositories(basePackages = {"io.muxin.office.server.repo.mssql"} ,
        entityManagerFactoryRef = "mssqlEntityManagerFactory")
public class MssqlRepositoryConfig {

    @Bean
    @ConfigurationProperties("spring.mssql.datasource")
    public DataSourceProperties mssqlDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix="spring.mssql.datasource")
    public DataSource mssqlDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean mssqlEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(mssqlDataSource())
                .packages("io.muxin.office.server.entity.mssql")
                .persistenceUnit("mssql")
                .build();
    }
}
