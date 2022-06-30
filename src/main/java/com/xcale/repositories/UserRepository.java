package com.xcale.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xcale.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
