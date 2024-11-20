package com.udemy.service;

import com.udemy.entity.User;
import java.util.List;

public interface UserService {

  List<User> findAll();

  User save(User user);

  boolean findByUsername(String username);
}
