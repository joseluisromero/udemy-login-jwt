package com.udemy.security.filter;

import static com.udemy.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.udemy.security.TokenJwtConfig.CONTENT_TYPE;
import static com.udemy.security.TokenJwtConfig.PREFIX_TOKEN;
import static com.udemy.security.TokenJwtConfig.SECRET_KEY;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udemy.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;


  public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    User user = null;
    String username = null;
    String password = null;
    try {
      user = new ObjectMapper().readValue(request.getInputStream(), User.class);
      username = user.getUsername();
      password = user.getPassword();

    } catch (IOException e) {
      log.error("error al obtener el usuario y password {}", e);
      throw new RuntimeException(e);
    }
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        username, password);
    return authenticationManager.authenticate(authenticationToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {
    org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
    String username = user.getUsername();
    Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
    Claims claims = Jwts.claims().add("authorities", new ObjectMapper().writeValueAsString(roles))
        .build();
    String token = Jwts.builder()
        .subject(username)
        .expiration(new Date(System.currentTimeMillis() + 3600000))
        .claims(claims)
        .issuedAt(new Date())
        .signWith(SECRET_KEY)
        .compact();
    response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

    Map<String, String> body = new HashMap<>();
    body.put("token", token);
    body.put("username", username);
    body.put("message", String.format("Hola %s has iniciado session con éxito!", username));
    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    response.setContentType(CONTENT_TYPE);
    response.setStatus(200);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {
    Map<String, String> body = new HashMap<>();
    body.put("message", "Error en la autenticacion username o password incorrecto");
    body.put("error", failed.getMessage());
    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    response.setContentType(CONTENT_TYPE);
    response.setStatus(401);
  }
}
