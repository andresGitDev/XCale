package com.xcale.models;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author  ngonzalez
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "Messages")
public class Message {
	@Id
    @GeneratedValue
	private Long id;
	
	@NotBlank(message = "Message is mandatory")
	private String message;

	@NotNull(message = "User can't be null")
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

	@NotNull(message = "Group can't be null")
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="group_id", nullable=false)
	private Group group;
	
	private LocalDateTime sentAt;
}
