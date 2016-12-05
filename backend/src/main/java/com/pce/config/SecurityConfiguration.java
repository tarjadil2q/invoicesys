package com.pce.config;

import com.google.common.collect.Sets;
import com.pce.constant.RoleConstant;
import com.pce.domain.Role;
import com.pce.domain.User;
import com.pce.domain.dto.UserDto;
import com.pce.service.RoleService;
import com.pce.service.UserService;
import com.pce.service.authentication.RestAuthenticationEntryPoint;
import com.pce.service.authentication.RestAuthenticationSuccessHandler;
import com.pce.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by Leonardo Tarjadi on 8/02/2016.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private RoleService roleService;

  @Autowired
  private UserService userService;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

  @Autowired
  private RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;

  @Autowired
  private SimpleUrlAuthenticationFailureHandler simpleUrlAuthenticationFailureHandler;
  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @Bean
  public SimpleUrlAuthenticationFailureHandler getSimpleUrlAuthenticationFailureHandler() {
    return new SimpleUrlAuthenticationFailureHandler();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {


    http.authorizeRequests()
            .antMatchers(HttpMethod.GET, "/api/**").authenticated()
            .antMatchers(HttpMethod.POST, "/api/**").authenticated()
            .antMatchers(HttpMethod.PUT, "/api/**").authenticated()
            .anyRequest().permitAll()
            .and()

            .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
            .and()
            .formLogin().successHandler(restAuthenticationSuccessHandler)
            .and()
            .formLogin().failureHandler(simpleUrlAuthenticationFailureHandler)
            .and()
            .csrf().disable()//Enable back when in prod mode
            .httpBasic().and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }


  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService)
            .passwordEncoder(bCryptPasswordEncoder);
    List<Role> roleByRoleNameIgnoreCase = roleService.findRoleByRoleNameIgnoreCase(RoleConstant.ADMIN.getRoleName());
    if (CollectionUtils.isEmpty(roleByRoleNameIgnoreCase)) {
      Role  adminRole = roleService.createOrUpdateRole(new Role(RoleConstant.ADMIN.getRoleName()));
      if (!userService.isUserExists("leonardo.tarjadi@gmail.com")){
        UserDto userDto = new UserDto();
        userDto.setEmail("leonardo.tarjadi@gmail.com");
        userDto.setFirstName("Leonardo");
        userDto.setLastName("Tarjadi");
        userDto.setPassword("gkypce");
        User user = userMapper.mapDtoIntoEntity(userDto);
        userService.createOrUpdate(user);
        userService.createOrUpdate(user, Sets.newHashSet(adminRole));
      }
    }




  }
}
