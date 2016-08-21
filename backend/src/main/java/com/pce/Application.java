package com.pce;

import com.pce.constant.RoleConstant;
import com.pce.domain.Role;
import com.pce.service.RoleService;
import com.pce.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by Leonardo Tarjadi on 3/02/2016.
 */

@ComponentScan(basePackages = "com.pce")
@EnableAutoConfiguration
@Configuration
@EnableJpaRepositories("com.pce.repository")
public class Application extends SpringBootServletInitializer {
  private static final Logger logger = LoggerFactory.getLogger(Application.class);


  @Autowired
  private UserServiceImpl userService;

  @Autowired
  private RoleService roleService;

  public static void main(String[] args) throws Exception {

    SpringApplication.run(Application.class, args);

  }


  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

    List<Role> roleByRoleNameIgnoreCase = roleService.findRoleByRoleNameIgnoreCase(RoleConstant.ADMIN.getRoleName());
    if (CollectionUtils.isEmpty(roleByRoleNameIgnoreCase)) {
      roleService.createOrUpdateRole(new Role(RoleConstant.ADMIN.getRoleName()));
    }

    return application.sources(Application.class);
  }

    /*@Override
    @Transactional
    public void run(String... args) throws Exception {
        UserCreationForm newUser = new UserCreationForm();
        newUser.setEmail("leonardo.tarjadi@gmail.com");
        newUser.setFirstName("Leonardo");
        newUser.setLastName("Tarjadi");
        newUser.setPassword("test");
        newUser.setRole(new Role(RoleConstant.ADMIN));


        userService.create(newUser);

        for (User user :  userRepository.findAll()){
            logger.info(user.toString());
        }

    }*/
}
