package com.udemy.service.impl;

import com.udemy.entity.Role;
import com.udemy.entity.User;
import com.udemy.repository.RoleRepository;
import com.udemy.repository.UserRepository;
import com.udemy.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  @Transactional(readOnly = true)
  public List<User> findAll() {
    return (List<User>) userRepository.findAll();
  }

  @Override
  @Transactional
  public User save(User user) {
    Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
    List<Role> roles = new ArrayList<>();
    optionalRoleUser.ifPresent(role -> roles.add(role));
    if (user.isAdmin()) {
      Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
      optionalRoleAdmin.ifPresent(role -> roles.add(role));
    }
    user.setRoles(roles);
    user.setEnabled(true);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    //user.setId(1L);
    user = userRepository.save(user);
    return user;
  }

  @Override
  public boolean findByUsername(String username) {
    return userRepository.existsByUsername(username);
  }
}
