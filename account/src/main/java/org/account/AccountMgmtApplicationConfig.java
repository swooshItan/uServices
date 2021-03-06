package org.account;

import java.time.Duration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.transaction.SystemException;

import com.atomikos.icatch.jta.UserTransactionManager;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class AccountMgmtApplicationConfig {

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.H2).build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.H2);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(false);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setDataSource(dataSource());
        factory.setPackagesToScan("org.account.domain");
        factory.setPersistenceUnitName("accountPU");
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) throws SystemException {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setTransactionTimeout(60);

        JtaTransactionManager txManager = new JtaTransactionManager();
        txManager.setTransactionManager(userTransactionManager);
        txManager.setUserTransaction(userTransactionManager);

        // JpaTransactionManager txManager = new JpaTransactionManager();
        // txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }

    @Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer(
		@Value("${resilience4j.circuitbreaker.failureRateThreshold}") int failureRateThreshold,
		@Value("${resilience4j.circuitbreaker.slowCallRateThreshold}") int slowCallRateThreshold,
		@Value("${resilience4j.circuitbreaker.slowCallDurationThreshold}") int slowCallDurationThreshold,
		@Value("${resilience4j.timelimiter.timeoutDuration}") int timeoutDuration) {

    	return factory -> {
			factory.configureDefault(id -> 
				new Resilience4JConfigBuilder(id)
					.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
					.circuitBreakerConfig(CircuitBreakerConfig.custom()
							.failureRateThreshold(failureRateThreshold)
							.slowCallRateThreshold(slowCallRateThreshold)
							.slowCallDurationThreshold(Duration.ofSeconds(slowCallDurationThreshold))
							.build())
					.timeLimiterConfig(TimeLimiterConfig.custom()
							.cancelRunningFuture(true)
							.timeoutDuration(Duration.ofSeconds(timeoutDuration))
							.build())
					.build());
		};
	}
}
