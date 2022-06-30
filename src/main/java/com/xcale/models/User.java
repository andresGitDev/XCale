package com.xcale.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "Users")
public class User {
	@Id
    @GeneratedValue
	private Long id;
	@NotBlank(message = "Name is mandatory")
	private String name;
	@NotBlank(message = "Lastname is mandatory")
	private String lastName;
	private String nickName;
	
	@NotNull(message = "Phone is mandatory")
	private String phone;
    
    @Builder.Default
	@ManyToMany(mappedBy = "members")
	public List<Group> groups = new ArrayList<Group>();

    @Builder.Default
	@OneToMany(mappedBy="owner")
    private List<Group> ownergroups = new ArrayList<Group>();

    @Builder.Default
	@OneToMany(mappedBy="user")
    private List<Message> messages = new ArrayList<Message>();
	
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;
	
}
