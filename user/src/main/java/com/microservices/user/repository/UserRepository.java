package com.microservices.user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservices.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  User findByUsername(String username);

  void deleteByUsername(String username);

}
