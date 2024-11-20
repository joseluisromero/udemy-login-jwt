package com.udemy.validation;

import com.udemy.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExistsByUsernameValidation implements ConstraintValidator<ExistsByUsername, String> {

  @Autowired
  private UserService userService;

  @Override
  public boolean isValid(String username, ConstraintValidatorContext context) {
    return !userService.findByUsername(username);
  }
}
