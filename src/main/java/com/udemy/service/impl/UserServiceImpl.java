package com.udemy.service.impl;

import com.udemy.entity.Role;
import com.udemy.entity.User;
import com.udemy.repository.RoleRepository;
import com.udemy.repository.UserRepository;
import com.udemy.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  private final PasswordEncoder passwordEncoder;

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
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }
}
