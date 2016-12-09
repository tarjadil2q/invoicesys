package com.pce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Leonardo Tarjadi on 3/02/2016.
 */

@ComponentScan(basePackages = "com.pce")
@EnableAutoConfiguration
@Configuration
@EnableJpaRepositories("com.pce.repository")
public class Application extends SpringBootServletInitializer {
  private static final Logger logger = LoggerFactory.getLogger(Application.class);


  public static void main(String[] args) throws Exception {

    SpringApplication.run(Application.class, args);

  }


}
