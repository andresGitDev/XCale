package com.xcale.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xcale.models.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {

}
