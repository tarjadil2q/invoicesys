package com.pce.validator;

import com.pce.domain.User;
import com.pce.domain.dto.UserCreationForm;
import com.pce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 8/02/2016.
 */
@Component
public class UserCreateValidator implements Validator {

    private UserService userService;

    @Autowired
    public UserCreateValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserCreationForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserCreationForm userCreationForm = (UserCreationForm) target;

        validatePasswords(userCreationForm, errors);
        validateEmail(userCreationForm, errors);
        validateRole(userCreationForm, errors);
    }

    private void validatePasswords(UserCreationForm userCreationForm, Errors errors) {
        if (!userCreationForm.getPassword().equals(userCreationForm.getPasswordRepeated())) {
            errors.reject("password.no_match", "Passwords do not match");
        }
    }

    private void validateEmail(UserCreationForm userCreationForm, Errors errors) {
        Optional<User> userByEmail = userService.getUserByEmail(userCreationForm.getEmail());
        if (userByEmail.isPresent()) {
            errors.reject("email.exists", "User with this email already exists");
        }
    }
    private void validateRole(UserCreationForm userCreationForm, Errors errors){
        if (CollectionUtils.isEmpty(userCreationForm.getSelectedRoleIds())){
            errors.reject("role.not.selected", "Please select at least one role");
        }
    }


}
