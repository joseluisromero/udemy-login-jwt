package com.udemy.repository;

import com.udemy.entity.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  boolean existsByUsername(String username);

  Optional<User> findByUsername(String username);
}
