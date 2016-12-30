package com.pce.config;

import com.pce.domain.*;
import com.pce.domain.dto.*;
import com.pce.service.converter.CalendarConverter;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by Leonardo Tarjadi on 6/02/2016.
 */
@Configuration
public class PCEConfiguration {

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.addConverter(new CalendarConverter());

    modelMapper.addMappings(new PropertyMap<PukGroup, PukGroupDto>() {
      @Override
      protected void configure() {
        when(Conditions.isNull()).skip().setPuks(null);
        when(Conditions.isNull()).skip().setPukGroupUsers(null);
      }
    });
    modelMapper.addMappings(new PropertyMap<User, UserDto>() {
      @Override
      protected void configure() {
        when(Conditions.isNull()).skip().setRoles(null);
        when(Conditions.isNull()).skip().setRecipientBankAccounts(null);
      }
    });
    modelMapper.addMappings(new PropertyMap<Pce, PceDto>() {

      @Override
      protected void configure() {
        when(Conditions.isNull()).skip().setApprovers(null);
        when(Conditions.isNull()).skip().setAssociatedPuk(null);
        when(Conditions.isNull()).skip().setPceItems(null);
        when(Conditions.isNull()).skip().setRecipientBankAccount(null);
      }
    });
    modelMapper.addMappings((new PropertyMap<Puk, PukDto>() {
      @Override
      protected void configure() {
        when(Conditions.isNull()).skip().setPukItems(null);
        when(Conditions.isNull()).skip().setPukGroup(null);
        when(Conditions.isNull()).skip().setAssociatedPces(null);

      }
    }));
    modelMapper.addMappings((new PropertyMap<PukItem, PukItemDto>() {
      @Override
      protected void configure() {
        when(Conditions.isNull()).skip().setPukItemMeasurement(null);
      }
    }));
    modelMapper.addMappings(new PropertyMap<RecipientBankAccount, RecipientBankAccountDto>() {
      @Override
      protected void configure() {
        when(Conditions.isNull()).skip().setAssociatedUser(null);
      }
    });
    return modelMapper;
  }

 /* @Bean
  public PropertyMap<RecipientBankAccountDto, RecipientBankAccount> getRecipientBankMap(){
    PropertyMap<RecipientBankAccountDto, RecipientBankAccount> recipientBankMap = new PropertyMap<RecipientBankAccountDto, RecipientBankAccount>() {
      protected void configure() {
        map().get(source.getAcctName());
        map().setAcctNumber(source.getAcctNumber());
        map().setBsb(source.getBsb());
        map().setRecipientBankAccountId(source.getRecipientBankAccountId());
      }
    };
    return recipientBankMap;
  }*/


  /*@Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
    reloadableResourceBundleMessageSource.setBasename("messages");
    return reloadableResourceBundleMessageSource;

  }*/
}
