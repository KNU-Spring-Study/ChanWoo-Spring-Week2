package com.springstudy.studypractice.repository;

import com.springstudy.studypractice.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    List<User> findAll();

    void delete(User user);
}
