package com.xcale.services;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.xcale.constants.Constants;
import com.xcale.models.Group;
import com.xcale.models.User;
import com.xcale.models.dtos.MemberDTO;
import com.xcale.repositories.GroupRepository;
import com.xcale.repositories.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Log
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class GroupService {

	private GroupRepository groupRepository;
	private UserRepository userRepository;

	public ResponseEntity<?> create(Group group){
		try {
			if(Optional.ofNullable(group.getId()).isPresent())
				throw new SQLException(Constants.EXIST_ID);
			
			userRepository.findById(group.getOwner().getId())
				.orElseThrow(() -> new NotFoundException());
			group.setCreatedAt(LocalDateTime.now());
			groupRepository.save(group);
			return new ResponseEntity<Group>(group,HttpStatus.CREATED);
		}
		catch (SQLException | NotFoundException e) {
			log.severe(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	public Optional<MemberDTO> add(MemberDTO member) throws SQLException {
		try {
			User user = userRepository.findById(member.getUserId())
					.orElseThrow(() -> new SQLException(Constants.USER_NOT_EXIST));
			Group group = groupRepository.findById(member.getGroupId())
					.orElseThrow(() -> new SQLException(Constants.GROUP_NOT_EXIST));
			List<Long> memebersId = group.getMembers().stream().map(User::getId)
					.collect(Collectors.toUnmodifiableList());
			if (memebersId.contains(user.getId()))
				throw new SQLException(Constants.USER_GROUP_EXIST);

			group.addMembers(user);
			groupRepository.save(group);
			return Optional.of(member);
		} catch (SQLException e) {
			log.severe(e.getMessage());
			throw e;
		}
	}
	
	public ResponseEntity<?> update(Group group){
		try {
			Long id = Optional.ofNullable(group.getId())
					.orElseThrow(() -> new SQLException(Constants.NOT_EXIST_ID));
			groupRepository.findById(id).orElseThrow(() -> new NotFoundException());
			group.setUpdatedAt(LocalDateTime.now());
			groupRepository.save(group);
			return new ResponseEntity<Group>(group,HttpStatus.OK);
		}
		catch (SQLException | NotFoundException e) {
			log.severe(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
	}
	
	public ResponseEntity<String> delete(Long groupId){
		try {
			Group group = groupRepository.findById(groupId)
					.orElseThrow(() -> new NotFoundException());
			group.setDeletedAt(LocalDateTime.now());
			groupRepository.save(group);
			return new ResponseEntity<String>(HttpStatus.OK.getReasonPhrase(),HttpStatus.OK);
		}
		catch (NotFoundException e) {
			log.severe(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
	}

}
