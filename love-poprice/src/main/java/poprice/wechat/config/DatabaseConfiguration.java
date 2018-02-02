package poprice.wechat.config;


import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import poprice.wechat.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableJpaRepositories("poprice.wechat.repository")
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
public class DatabaseConfiguration implements EnvironmentAware {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    private RelaxedPropertyResolver dataSourcePropertyResolver;

    private Environment env;

    @Autowired(required = false)
    private MetricRegistry metricRegistry;

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
        this.dataSourcePropertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
    }

    @Bean(destroyMethod = "shutdown")
    @Profile("!" + Constants.SPRING_PROFILE_CLOUD)
    public DataSource dataSource() {
        log.debug("Configuring Datasource");
        if (dataSourcePropertyResolver.getProperty("url") == null && dataSourcePropertyResolver.getProperty("databaseName") == null) {
            log.error("Your database connection pool configuration is incorrect! The application" +
                            " cannot start. Please check your Spring profile, current profiles are: {}",
                    Arrays.toString(env.getActiveProfiles()));

            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(dataSourcePropertyResolver.getProperty("dataSourceClassName"));
        if(StringUtils.isEmpty(dataSourcePropertyResolver.getProperty("url"))) {
            config.addDataSourceProperty("databaseName", dataSourcePropertyResolver.getProperty("databaseName"));
            config.addDataSourceProperty("serverName", dataSourcePropertyResolver.getProperty("serverName"));
        } else {
            config.addDataSourceProperty("url", dataSourcePropertyResolver.getProperty("url"));
        }
        config.addDataSourceProperty("user", dataSourcePropertyResolver.getProperty("username"));
        config.addDataSourceProperty("password", dataSourcePropertyResolver.getProperty("password"));

        config.setMaximumPoolSize(Integer.valueOf(dataSourcePropertyResolver.getProperty("maximumPoolSize")));
        config.setMinimumIdle(Integer.valueOf(dataSourcePropertyResolver.getProperty("minimumIdle")));

        //设置详情: https://github.com/brettwooldridge/HikariCP
        config.setConnectionTimeout(10000L); //最大获取等待时间, 减少到默认的1/3, 防止界面过度等待.


        //MySQL optimizations, see https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
        if ("com.mysql.jdbc.jdbc2.optional.MysqlDataSource".equals(dataSourcePropertyResolver.getProperty("dataSourceClassName"))) {
            config.addDataSourceProperty("cachePrepStmts", dataSourcePropertyResolver.getProperty("cachePrepStmts", "true"));
            config.addDataSourceProperty("prepStmtCacheSize", dataSourcePropertyResolver.getProperty("prepStmtCacheSize", "250"));
            config.addDataSourceProperty("prepStmtCacheSqlLimit", dataSourcePropertyResolver.getProperty("prepStmtCacheSqlLimit", "2048"));
        }
        if (metricRegistry != null) {
            config.setMetricRegistry(metricRegistry);
        }
        return new HikariDataSource(config);
    }


    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }


}
