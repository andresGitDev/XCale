package com.xcale.services;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.xcale.constants.Constants;
import com.xcale.models.User;
import com.xcale.repositories.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Log
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

	private UserRepository userRepository;

	public ResponseEntity<?> create(User user){
		try {
			if(Optional.ofNullable(user.getId()).isPresent())
				throw new SQLException(Constants.EXIST_ID);
			
			user.setCreatedAt(LocalDateTime.now());
			userRepository.save(user);
			return new ResponseEntity<User>(user,HttpStatus.CREATED);
		}
		catch (SQLException e) {
			log.severe(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	public ResponseEntity<?> update(User user){
		try {
			Long id = Optional.ofNullable(user.getId())
					.orElseThrow(() -> new SQLException(Constants.NOT_EXIST_ID));
			userRepository.findById(id).orElseThrow(() -> new NotFoundException());
			user.setUpdatedAt(LocalDateTime.now());
			userRepository.save(user);
			return new ResponseEntity<User>(user,HttpStatus.OK);
		}
		catch (SQLException | NotFoundException e) {
			log.severe(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
	}
	
	public ResponseEntity<String> delete(Long userId){
		try {
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new NotFoundException());
			user.setDeletedAt(LocalDateTime.now());
			userRepository.save(user);
			return new ResponseEntity<String>(HttpStatus.OK.getReasonPhrase(),HttpStatus.OK);
		}
		catch (NotFoundException e) {
			log.severe(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
	}

}
