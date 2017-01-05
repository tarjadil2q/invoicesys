package com.pce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Created by Leonardo Tarjadi on 3/02/2016.
 */

@ComponentScan(basePackages = "com.pce")
@EnableAutoConfiguration
@Configuration
@EnableJpaRepositories("com.pce.repository")
@SpringBootApplication
@EnableAsync
public class Application extends AsyncConfigurerSupport {
  private static final Logger logger = LoggerFactory.getLogger(Application.class);


  public static void main(String[] args) throws Exception {

    SpringApplication.run(Application.class, args);
  }

  @Override
  public Executor getAsyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(2);
    executor.setMaxPoolSize(5);
    executor.setQueueCapacity(100);
    executor.setThreadNamePrefix("SendingMail:-");
    executor.initialize();
    return executor;
  }
}
