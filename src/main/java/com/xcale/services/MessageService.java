package com.xcale.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xcale.models.Message;
import com.xcale.repositories.MessageRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MessageService {

	private MessageRepository messageRepository;

	public Message saveMessage(Message message){
		message.setSentAt(LocalDateTime.now());
		return messageRepository.save(message);
	}

}
