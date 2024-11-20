package com.udemy.security;

import com.udemy.security.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

  @Autowired
  private AuthenticationConfiguration authenticationConfiguration;

  @Bean
  AuthenticationManager authenticationManager() throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
            .requestMatchers(HttpMethod.POST, "/login").permitAll()
            .requestMatchers("/h2-console/**").permitAll() // Permite acceso a la consola H2
            .anyRequest().authenticated()
        )
        .addFilter(new JwtAuthenticationFilter(authenticationManager()))
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/h2-console/**") // Ignora CSRF para la consola H2
            .disable()
        )
        .headers(headers -> headers
            .frameOptions().sameOrigin() // Permite que la consola H2 se cargue en un iframe
        )
        .sessionManagement(management -> management
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .build();

  }
  /*@Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests( (authz) -> authz
            .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
            .anyRequest().authenticated())
        .csrf(config -> config.disable())
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
  }*/
}
