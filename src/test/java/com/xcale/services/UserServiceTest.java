package com.xcale.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.xcale.constants.Constants;
import com.xcale.models.User;
import com.xcale.repositories.UserRepository;

/**
 * Tests for{@link UserService}
 *
 * @author ngonzalez
 */

@SpringBootTest
public class UserServiceTest{

    @Autowired
    private UserService userService;
    
    @MockBean
	private UserRepository userRepository;

	private User user, userId;
	
	@BeforeEach
	public void setup(){
		this.user = User.builder().name("Nicolas").build();
		this.userId = User.builder().name("Nicolas").id(1L).build();
	}
	
	@Test
	public void createUserAndReturnCreated(){
		Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		ResponseEntity<?> responseEntity = userService.create(user);
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
	}
	
	@Test
	public void createUserWithIdAndReturnBadRequest(){
		ResponseEntity<?> responseEntity = userService.create(userId);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals(responseEntity.getBody().toString(), Constants.EXIST_ID);
	}
	
	@Test
	public void updateUserAndReturnOK() {
		Mockito.when(userRepository.findById(userId.getId())).thenReturn(Optional.of(userId));
		ResponseEntity<?> responseEntity = userService.update(userId);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
	
	@Test
	public void updateUserWithIdAndReturnBadRequest() {
		ResponseEntity<?> responseEntity = userService.update(user);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals(responseEntity.getBody().toString(), Constants.NOT_EXIST_ID);
	}
	
	@Test
	public void updateUserAndReturnNotFound() {
		Mockito.when(userRepository.findById(userId.getId())).thenReturn(Optional.ofNullable(null));
		ResponseEntity<?> responseEntity = userService.update(userId);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	public void deleteUserAndReturnOK() {
		Mockito.when(userRepository.findById(userId.getId())).thenReturn(Optional.of(userId));
		ResponseEntity<?> responseEntity = userService.delete(userId.getId());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(responseEntity.getBody().toString(), HttpStatus.OK.getReasonPhrase());
	}
		
	@Test
	public void deleteUserAndReturnNotFound() {
		Mockito.when(userRepository.findById(userId.getId())).thenReturn(Optional.ofNullable(null));
		ResponseEntity<?> responseEntity = userService.delete(userId.getId());
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

}

