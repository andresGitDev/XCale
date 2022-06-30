package com.xcale.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcale.constants.Constants;
import com.xcale.models.User;
import com.xcale.services.UserService;

/**
 * Tests for {@link GroupController}
 *
 * @author ngonzalez
 */

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    
    @MockBean
	private UserService userService;

    private User user;

	@BeforeEach
	public void setup() {
		this.user = User.builder().id(1L).name("Nicolas").lastName("Gonzalez")
				.phone("(549) 11-2173568").build();
	}
	
	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void createUserAndReturnOK() throws Exception {
		ResponseEntity responseEntity = new ResponseEntity<User>(user,HttpStatus.CREATED);
		Mockito.when(userService.create(user)).thenReturn(responseEntity);
		
		mvc.perform(MockMvcRequestBuilders.post(Constants.PATH_REQUEST_USER)
				.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
        .andExpect(status().isCreated());
	}
	
	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void createUserAndReturnBadRequest() throws Exception {
		ResponseEntity responseEntity = new ResponseEntity<String>(Constants.EXIST_ID,HttpStatus.BAD_REQUEST);
		Mockito.when(userService.create(user)).thenReturn(responseEntity);
		
		mvc.perform(MockMvcRequestBuilders.post(Constants.PATH_REQUEST_USER)
				.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
        .andExpect(status().isBadRequest());
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateUserAndReturnOK() throws Exception {
		ResponseEntity responseEntity = new ResponseEntity<User>(user,HttpStatus.OK);
		Mockito.when(userService.update(user)).thenReturn(responseEntity);
		
		mvc.perform(MockMvcRequestBuilders.put(Constants.PATH_REQUEST_USER)
				.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        		.content(new ObjectMapper().writeValueAsString(user)))
        .andExpect(status().isOk());
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateUserAndReturnNotFound() throws Exception {
		ResponseEntity responseEntity = new ResponseEntity<String>(Constants.NOT_EXIST_ID,HttpStatus.NOT_FOUND);
		Mockito.when(userService.update(user)).thenReturn(responseEntity);
		
		mvc.perform(MockMvcRequestBuilders.put(Constants.PATH_REQUEST_USER)
				.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        		.content(new ObjectMapper().writeValueAsString(user)))
        .andExpect(status().isNotFound());
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteUserAndReturnOK() throws Exception {
		ResponseEntity responseEntity = new ResponseEntity<String>(HttpStatus.OK.getReasonPhrase(),HttpStatus.OK);
		Mockito.when(userService.delete(user.getId())).thenReturn(responseEntity);
		mvc.perform(MockMvcRequestBuilders.delete(String.format(Constants.PATH_REQUEST_USER.concat("/%s"), user.getId()))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteUserAndReturnNotFound() throws Exception {
		ResponseEntity responseEntity = new ResponseEntity<String>(HttpStatus.NOT_FOUND.getReasonPhrase(),HttpStatus.NOT_FOUND);
		Mockito.when(userService.delete(user.getId())).thenReturn(responseEntity);
		mvc.perform(MockMvcRequestBuilders.delete(String.format(Constants.PATH_REQUEST_USER.concat("/%s"), user.getId()))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
	}
}
