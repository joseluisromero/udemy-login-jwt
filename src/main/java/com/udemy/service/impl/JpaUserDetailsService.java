package com.udemy.service.impl;

import com.udemy.entity.User;
import com.udemy.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userOptional = userRepository.findByUsername(username);
    if (userOptional.isEmpty()) {
      throw new UsernameNotFoundException(
          String.format("Username %s no existe en el sistema!", username));
    }
    User user = userOptional.get();
    List<GrantedAuthority> authorities = user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getName())).collect(
            Collectors.toList());

    return new org.springframework.security.core.userdetails.User(user.getUsername(),
        user.getPassword(),//aqui viene  sin encriptar y se  compara con la db
        user.isEnabled(),
        true,
        true,
        true,
        authorities);
  }
}
