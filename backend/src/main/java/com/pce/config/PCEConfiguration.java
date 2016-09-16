package com.pce.config;

import com.pce.service.converter.CalendarConverter;
import org.modelmapper.ModelMapper;
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
    // modelMapper.addMappings(getRecipientBankMap());
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
