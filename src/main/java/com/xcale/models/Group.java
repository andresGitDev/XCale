package com.xcale.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
 * @author ngonzalez
 */

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "Groups")
public class Group {
	
	@Id
	@GeneratedValue
	private Long id;
	@NotBlank(message = "Name is mandatory")
	private String name;

    @Builder.Default
	@ManyToMany(cascade = { CascadeType.ALL, CascadeType.MERGE })
	@JoinTable(name = "Group_users", 
		joinColumns = @JoinColumn(name = "group_id"), 
		inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> members =  new ArrayList<User>();

	@NotNull(message = "Owner can't be null")
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
	private User owner;

    @Builder.Default
	@OneToMany(mappedBy="group")
    private List<Message> messages = new ArrayList<Message>();
    
    private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;

	public void addMembers(User user) {
		members.add(user);
		user.getGroups().add(this);
	}

	public void removeMembers(User user) {
		members.remove(user);
		user.getGroups().remove(this);
	}

	public void addMessage(Message message) {
		messages.add(message);
		message.setGroup(this);
	}

}
