package com.xcale.controllers;

import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.xcale.models.Message;
import com.xcale.models.dtos.MemberDTO;
import com.xcale.services.GroupService;
import com.xcale.services.MessageService;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "Message Controller")
public class MessageWebSocketController {
	
	private MessageService messageService;
	private GroupService groupService;
	
	@MessageMapping("/register")
    @SendTo("/topic/public")
    public MemberDTO register(MemberDTO member, SimpMessageHeaderAccessor headerAccessor) throws SQLException{
		Optional<MemberDTO> memberResponse = groupService.add(member);
		headerAccessor.getSessionAttributes().put("userid", member.getUserId());
        return memberResponse.get();
    }
	
    @MessageMapping("/send")
    @SendTo("/topic/public")
    public Message sendMessage(Message message) {
    	return messageService.saveMessage(message);
    }

}
