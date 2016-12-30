package com.pce.validation.validator;


import com.pce.domain.User;
import com.pce.domain.dto.UserDto;
import com.pce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 30/12/2016.
 */
@Component
public class RecipientUserAssociatedValidator implements Validator {
  @Autowired
  private UserService userService;

  @Override
  public boolean supports(Class<?> clazz) {
    return UserDto.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    UserDto userDto = (UserDto) target;
    if (userDto == null) {
      errors.reject("userDto.not.exists", "User  is not being set, please set");
      return;
    }
    long userId = userDto.getUserId();
    Optional<User> userById = userService.getUserById(userId);
    if (!userById.isPresent()) {
      errors.rejectValue("userId", "puk.not.exists", "User with ID  " + userId + " is not exists in the system, please select different one");
    }
  }
}
