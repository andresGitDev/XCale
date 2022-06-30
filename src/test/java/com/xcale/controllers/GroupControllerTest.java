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
import com.xcale.models.Group;
import com.xcale.models.User;
import com.xcale.services.GroupService;

/**
 * Tests for {@link GroupController}
 *
 * @author ngonzalez
 */

@SpringBootTest
@AutoConfigureMockMvc
class GroupControllerTest {

    @Autowired
    private MockMvc mvc;
    
    @MockBean
	private GroupService groupService;

    private Group group;

	@BeforeEach
	public void setup() {
		this.group = Group.builder().name("New group").owner(new User()).id(1L).build();
	}
	
	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void createGroupAndReturnOK() throws Exception {
		ResponseEntity responseEntity = new ResponseEntity<Group>(group,HttpStatus.CREATED);
		Mockito.when(groupService.create(group)).thenReturn(responseEntity);
		
		mvc.perform(MockMvcRequestBuilders.post(Constants.PATH_REQUEST_GROUP)
				.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(group)))
        .andExpect(status().isCreated());
	}
	
	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void createGroupAndReturnBadRequest() throws Exception {
		ResponseEntity responseEntity = new ResponseEntity<String>(Constants.EXIST_ID,HttpStatus.BAD_REQUEST);
		Mockito.when(groupService.create(group)).thenReturn(responseEntity);
		
		mvc.perform(MockMvcRequestBuilders.post(Constants.PATH_REQUEST_GROUP)
				.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(group)))
        .andExpect(status().isBadRequest());
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateGroupAndReturnOK() throws Exception {
		ResponseEntity responseEntity = new ResponseEntity<Group>(group,HttpStatus.OK);
		Mockito.when(groupService.update(group)).thenReturn(responseEntity);
		
		mvc.perform(MockMvcRequestBuilders.put(Constants.PATH_REQUEST_GROUP)
				.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        		.content(new ObjectMapper().writeValueAsString(group)))
        .andExpect(status().isOk());
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateGroupAndReturnNotFound() throws Exception {
		ResponseEntity responseEntity = new ResponseEntity<String>(Constants.NOT_EXIST_ID,HttpStatus.NOT_FOUND);
		Mockito.when(groupService.update(group)).thenReturn(responseEntity);
		
		mvc.perform(MockMvcRequestBuilders.put(Constants.PATH_REQUEST_GROUP)
				.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        		.content(new ObjectMapper().writeValueAsString(group)))
        .andExpect(status().isNotFound());
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteGroupAndReturnOK() throws Exception {
		ResponseEntity responseEntity = new ResponseEntity<String>(HttpStatus.OK.getReasonPhrase(),HttpStatus.OK);
		Mockito.when(groupService.delete(group.getId())).thenReturn(responseEntity);
		mvc.perform(MockMvcRequestBuilders.delete(String.format(Constants.PATH_REQUEST_GROUP.concat("/%s"), group.getId()))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteGroupAndReturnNotFound() throws Exception {
		ResponseEntity responseEntity = new ResponseEntity<String>(HttpStatus.NOT_FOUND.getReasonPhrase(),HttpStatus.NOT_FOUND);
		Mockito.when(groupService.delete(group.getId())).thenReturn(responseEntity);
		mvc.perform(MockMvcRequestBuilders.delete(String.format(Constants.PATH_REQUEST_GROUP.concat("/%s"), group.getId()))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
	}
}
