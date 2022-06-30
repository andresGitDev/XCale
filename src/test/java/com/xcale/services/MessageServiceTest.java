package com.xcale.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.xcale.models.Group;
import com.xcale.models.Message;
import com.xcale.models.User;
import com.xcale.repositories.MessageRepository;

/**
 * Tests for{@link MessageService}
 *
 * @author ngonzalez
 */

@SpringBootTest
public class MessageServiceTest{

    @Autowired
    private MessageService messageService;
    
    @MockBean
	private MessageRepository messageRepository;

	private Message message;
	
	@BeforeEach
	public void setup(){
		User user = User.builder().id(1L).name("Nicolas").build();
		Group group = Group.builder().id(1L).name("new Group").build();
		this.message = Message.builder().user(user).group(group).message("Hi").build();
	}
	
	@Test
	public void saveMessage(){
		Mockito.when(messageRepository.save(message)).thenReturn(message);
		Message response = messageService.saveMessage(message);
		assertEquals(message.getMessage(), response.getMessage());
	}

}

