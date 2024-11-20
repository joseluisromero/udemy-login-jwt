package com.udemy.controller;

import com.udemy.entity.User;
import com.udemy.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/saludo")
public class SaludoController {

  @GetMapping
  public ResponseEntity<String> product() {
    return new ResponseEntity<>("Hola ", HttpStatus.OK);
  }
}
