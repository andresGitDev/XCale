package com.xcale.controllers;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xcale.constants.Constants;
import com.xcale.models.Group;
import com.xcale.services.GroupService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(Constants.PATH_REQUEST_GROUP)
@Api(value = "Groups Controller")
public class GroupController {
	
	private GroupService groupService;
	
	@ApiOperation(value = "Create a group with an owner")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = Constants.EXIST_ID) })
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> create(@RequestBody @Valid Group group) {
		return groupService.create(group);
	}

	@ApiOperation(value = "Update a group with an owner")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = Constants.NOT_EXIST_ID) })
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateGroup(@RequestBody @Valid Group group) {
		return groupService.update(group);
	}

	@ApiOperation(value = "Update a group with an owner")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = Constants.GROUP_NOT_EXIST) })
	@DeleteMapping(value = Constants.GROUP_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteGroup(@PathVariable(value = "groupId") @Min(1) Long groupId) {
		return groupService.delete(groupId);
	}
}
