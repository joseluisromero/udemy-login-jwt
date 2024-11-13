package com.udemy.controller;

import com.udemy.entity.User;
import com.udemy.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/users")
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<List<User>> getUsers() {
    return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<User> saveUsers(@Valid @RequestBody User user) {
    return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
  }
}
