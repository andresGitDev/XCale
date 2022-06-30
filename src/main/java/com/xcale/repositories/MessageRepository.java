package com.xcale.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xcale.models.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
