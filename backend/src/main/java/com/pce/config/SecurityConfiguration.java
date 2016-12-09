package com.pce.config;

import com.google.common.collect.Sets;
import com.pce.constant.RoleConstant;
import com.pce.domain.*;
import com.pce.domain.dto.*;
import com.pce.service.*;
import com.pce.service.authentication.RestAuthenticationEntryPoint;
import com.pce.service.authentication.RestAuthenticationSuccessHandler;
import com.pce.service.mapper.UserMapper;
import org.modelmapper.ModelMapper;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
  private PukGroupService pukGroupService;

  @Autowired
  private PukService pukService;

  @Autowired
  private PceApprovalRoleService pceApprovalRoleService;


  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private PukItemMeasurementService pukItemMeasurementService;

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
      Role adminRole = roleService.createOrUpdateRole(new Role(RoleConstant.ADMIN.getRoleName()));
      Role chairman = roleService.createOrUpdateRole(new Role("Chairman"));
      Role headOfFinance = roleService.createOrUpdateRole(new Role("Head of Finance"));
      Role financeComittee = roleService.createOrUpdateRole(new Role("Finance Comittee"));
      Role comitteeHead = roleService.createOrUpdateRole(new Role(RoleConstant.COMITTEE_HEAD.getRoleName()));
      if (!userService.isUserExists("leonardo.tarjadi@gmail.com")) {
        UserDto userDto = new UserDto();
        userDto.setEmail("leonardo.tarjadi@gmail.com");
        userDto.setFirstName("Leonardo");
        userDto.setLastName("Tarjadi");
        userDto.setPassword("gkypce");
        User user = userMapper.mapDtoIntoEntity(userDto);
        userService.createOrUpdate(user);
        userService.createOrUpdate(user, Sets.newHashSet(adminRole));

        UserDto chairmanUser = new UserDto();
        chairmanUser.setEmail("dedy.wikarsa@gmail.com");
        chairmanUser.setFirstName("Dedy");
        chairmanUser.setLastName("Wikarsa");
        chairmanUser.setPassword("gkypce");
        user = userMapper.mapDtoIntoEntity(chairmanUser);

        userService.createOrUpdate(user);
        userService.createOrUpdate(user, Sets.newHashSet(chairman));

        UserDto headOfFinanceUser = new UserDto();
        headOfFinanceUser.setEmail("heny.tan@gmail.com");
        headOfFinanceUser.setFirstName("Heny");
        headOfFinanceUser.setLastName("Tan");
        headOfFinanceUser.setPassword("gkypce");
        user = userMapper.mapDtoIntoEntity(headOfFinanceUser);

        userService.createOrUpdate(user);
        userService.createOrUpdate(user, Sets.newHashSet(headOfFinance));

        UserDto financeUser = new UserDto();
        financeUser.setEmail("merlin.kandaw@gmail.com");
        financeUser.setFirstName("Merlin");
        financeUser.setLastName("Kandaw");
        financeUser.setPassword("gkypce");
        user = userMapper.mapDtoIntoEntity(financeUser);

        userService.createOrUpdate(user);
        userService.createOrUpdate(user, Sets.newHashSet(financeComittee));

        UserDto comitteHeadUser = new UserDto();
        comitteHeadUser.setFirstName("Bambang");
        comitteHeadUser.setLastName("Kurnia");
        comitteHeadUser.setEmail("bambang.kurnia@gmail.com");
        comitteHeadUser.setPassword("gkypce");
        user = userMapper.mapDtoIntoEntity(comitteHeadUser);

        userService.createOrUpdate(user);
        userService.createOrUpdate(user, Sets.newHashSet(comitteeHead));


        PukItemMeasurementDto pukItemMeasurementDto = new PukItemMeasurementDto();
        pukItemMeasurementDto.setTypeOfMeasurement("Piece");
        PukItemMeasurement pukItemMeasurement = modelMapper.map(pukItemMeasurementDto, PukItemMeasurement.class);
        PukItemMeasurement pukItemMeasure = pukItemMeasurementService.createOrUpdatePukItemMeasurement(pukItemMeasurement);

        //create puk group
        PukGroupDto pukGroupDto = new PukGroupDto();
        pukGroupDto.setPukGroupName("SARPRAS-Hospitality");
        pukGroupDto.setPukGroupDescription("Semua yang berhubungan dengan alat alat rumah tangga gereja");

        Set pukGroupUsers = new HashSet<>();
        pukGroupUsers.add(userDto);
        pukGroupUsers.add(comitteHeadUser);

        pukGroupDto.setPukGroupUsers(pukGroupUsers);

        PukGroup sarprasHospitalityGroup = pukGroupService.createOrUpdatePukGroup(modelMapper.map(pukGroupDto, PukGroup.class));

        PukDto sarprasCutleryPuk = new PukDto();
        sarprasCutleryPuk.setPukNo("01/SARPRAS/SYD");
        sarprasCutleryPuk.setPukDescription("Puk Untuk Cutlery reimburshment");
        PukGroupForPukDto pukGroupForPukDto = new PukGroupForPukDto();
        pukGroupForPukDto.setPukGroupId(sarprasHospitalityGroup.getPukGroupId());
        sarprasCutleryPuk.setPukGroup(pukGroupForPukDto);
        sarprasCutleryPuk.setPukYear(Year.now().getValue());

        Puk createdPuk = pukService.createOrUpdatePuk(modelMapper.map(sarprasCutleryPuk, Puk.class));


        PukItemDto pukItemDto = new PukItemDto();
        pukItemDto.setActivityName("Cutlery");
        pukItemDto.setPerMeasurementPrice(new BigDecimal(2));
        pukItemDto.setQuantity(100);
        pukItemDto.setTotalActivity(1);
        PukItemMeasurementDto pukMeasure = new PukItemMeasurementDto();
        pukMeasure.setPukItemMeasurementId(pukItemMeasure.getPukItemMeasurementId());
        pukItemDto.setPukItemMeasurement(pukMeasure);
        pukService.createOrUpdatePukItem(pukService.getPukByPukId(createdPuk.getPukId()).get(), modelMapper.map(pukItemDto, PukItem.class));

        PceApprovalRoleDto pceApprovalRoleDto = new PceApprovalRoleDto();


        pceApprovalRoleDto.setRoleId(comitteeHead.getId());
        pceApprovalRoleDto.setApprovalRoleSequence(1);

        pceApprovalRoleService.createOrUpdatePceApprovalRole(modelMapper.map(pceApprovalRoleDto, PceApprovalRole.class));


        pceApprovalRoleDto.setRoleId(chairman.getId());
        pceApprovalRoleDto.setApprovalRoleSequence(2);

        pceApprovalRoleService.createOrUpdatePceApprovalRole(modelMapper.map(pceApprovalRoleDto, PceApprovalRole.class));


        pceApprovalRoleDto.setRoleId(financeComittee.getId());
        pceApprovalRoleDto.setApprovalRoleSequence(3);

        pceApprovalRoleService.createOrUpdatePceApprovalRole(modelMapper.map(pceApprovalRoleDto, PceApprovalRole.class));


        pceApprovalRoleDto.setRoleId(headOfFinance.getId());
        pceApprovalRoleDto.setApprovalRoleSequence(4);

        pceApprovalRoleService.createOrUpdatePceApprovalRole(modelMapper.map(pceApprovalRoleDto, PceApprovalRole.class));
      }

    }


  }
}
