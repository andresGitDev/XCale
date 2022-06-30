package com.xcale.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.xcale.constants.Constants;
import com.xcale.models.Group;
import com.xcale.models.User;
import com.xcale.models.dtos.MemberDTO;
import com.xcale.repositories.GroupRepository;
import com.xcale.repositories.UserRepository;

/**
 * Tests for{@link GroupService}
 *
 * @author ngonzalez
 */

@SpringBootTest
public class GroupServiceTest{

    @Autowired
    private GroupService groupService;

    @MockBean
	private GroupRepository groupRepository;
    
    @MockBean
	private UserRepository userRepository;

	private User user;
	private Group group, groupId, groupMembers;
	private MemberDTO member; 
	
	@BeforeEach
	public void setup(){
		this.user = User.builder().id(1L).build();
		this.group = Group.builder().owner(user).build();
		this.groupId = Group.builder().id(1L).owner(user).build();
		this.groupMembers = Group.builder().id(2L).owner(user).members(List.of(user)).build();
		this.member = MemberDTO.builder().userId(user.getId()).groupId(groupId.getId()).build();
	}
	
	@Test
	public void createGroupAndReturnCreated(){
		Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		ResponseEntity<?> responseEntity = groupService.create(group);
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
	}
	
	@Test
	public void createGroupWithIdAndReturnBadRequest(){
		ResponseEntity<?> responseEntity = groupService.create(groupId);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals(responseEntity.getBody().toString(), Constants.EXIST_ID);
	}
	
	@Test
	public void createGroupAndReturnNotFound() {
		Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(null));
		ResponseEntity<?> responseEntity = groupService.create(group);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Test
	public void updateGroupAndReturnOK() {
		Mockito.when(groupRepository.findById(groupId.getId())).thenReturn(Optional.of(groupId));
		ResponseEntity<?> responseEntity = groupService.update(groupId);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
	
	@Test
	public void updateGroupWithIdAndReturnBadRequest() {
		ResponseEntity<?> responseEntity = groupService.update(group);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals(responseEntity.getBody().toString(), Constants.NOT_EXIST_ID);
	}
	
	@Test
	public void updateGroupAndReturnNotFound() {
		Mockito.when(groupRepository.findById(groupId.getId())).thenReturn(Optional.ofNullable(null));
		ResponseEntity<?> responseEntity = groupService.update(groupId);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	public void addGroupAndReturnOK() throws SQLException {
		Mockito.when(userRepository.findById(member.getUserId())).thenReturn(Optional.of(user));
		Mockito.when(groupRepository.findById(member.getGroupId())).thenReturn(Optional.of(groupId));
		
		Optional<MemberDTO> response = groupService.add(member);
		assertNotNull(response.get());
	}
		
	@Test
	public void addGroupAndReturnUserNotFound() {
		Mockito.when(userRepository.findById(member.getUserId())).thenReturn(Optional.ofNullable(null));
		Assertions.assertThrows(SQLException.class, () -> groupService.add(member));
	}
	
	@Test
	public void addGroupAndReturnGroupNotFound() {
		Mockito.when(userRepository.findById(member.getUserId())).thenReturn(Optional.of(user));
		Mockito.when(groupRepository.findById(member.getGroupId())).thenReturn(Optional.ofNullable(null));
		Assertions.assertThrows(SQLException.class, () -> groupService.add(member));
	}
	
	@Test
	public void addGroupAndReturnUserGroupExist() {
		Mockito.when(userRepository.findById(member.getUserId())).thenReturn(Optional.of(user));
		Mockito.when(groupRepository.findById(member.getGroupId())).thenReturn(Optional.of(groupMembers));
		Assertions.assertThrows(SQLException.class, () -> groupService.add(member));
	}
	
	@Test
	public void deleteGroupAndReturnOK() {
		Mockito.when(groupRepository.findById(groupId.getId())).thenReturn(Optional.of(groupId));
		ResponseEntity<?> responseEntity = groupService.delete(groupId.getId());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(responseEntity.getBody().toString(), HttpStatus.OK.getReasonPhrase());
	}
		
	@Test
	public void deleteGroupAndReturnNotFound() {
		Mockito.when(groupRepository.findById(groupId.getId())).thenReturn(Optional.ofNullable(null));
		ResponseEntity<?> responseEntity = groupService.delete(groupId.getId());
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

}

