package io.muxin.office.server.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

/**
 * MYSQL 数据源配置
 * Created by muxin on 2017/2/23.
 */
@Configuration
@EnableJpaRepositories(basePackages = {"io.muxin.office.server.repo.mysql"} ,
        entityManagerFactoryRef = "mysqlEntityManagerFactory")
public class MysqlRepositoryConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.mysql.datasource")
    public DataSourceProperties mysqlDataSourceProperties() {
        return new DataSourceProperties();
    }
    @Bean
    @Primary
    @ConfigurationProperties(prefix="spring.mysql.datasource")
    public DataSource mysqlDataSource(){
        return DataSourceBuilder.create().type(ComboPooledDataSource.class).build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(mysqlDataSource())
                .packages("io.muxin.office.server.entity.mysql")
                .persistenceUnit("mysql")
                .build();
    }
}
