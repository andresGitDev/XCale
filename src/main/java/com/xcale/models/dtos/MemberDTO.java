package com.xcale.models.dtos;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
	
	@NotBlank(message = "GroupId is mandatory")
	private Long groupId;
	
	@NotBlank(message = "UserId is mandatory")
	private Long userId;
}
