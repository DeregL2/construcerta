package com.construcerta.repository;

import com.construcerta.model.Role;
import com.construcerta.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    long countByRolesContaining(Role role);

    List<User> findAllByOrderByUsernameAsc();
}
