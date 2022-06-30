package com.xcale.controllers;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.xcale.models.Group;
import com.xcale.models.Message;
import com.xcale.models.User;
import com.xcale.models.dtos.MemberDTO;
import com.xcale.services.GroupService;
import com.xcale.services.MessageService;

/**
 * Tests for {@link MessageWebSocketController}
 *
 * @author ngonzalez
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MessageWebSocketControllerTest {

    @MockBean
    private GroupService groupService;
    
    @MockBean
    private MessageService messageService;

	private User user;
	private Group group;
	private Message message;
	private MemberDTO member;
	private WebSocketStompClient webSocketStompClient;
    private CompletableFuture<Message> completableFutureMessage;
    private CompletableFuture<MemberDTO> completableFutureMember;
    private StompFrameHandler stompFrameHandlerMessage;
    private StompFrameHandler stompFrameHandlerMemberDTO;
	
	private String PATH = "ws://localhost:8080/XCale/ws-xcale";

	@BeforeEach
	public void setup() {
		this.webSocketStompClient = new WebSocketStompClient(
				new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient()))));
		this.message = Message.builder().message("Hello").user(user).group(group).build();
		this.member = MemberDTO.builder().userId(1L).groupId(1L).build();
        this.completableFutureMember = new CompletableFuture<>();
        this.completableFutureMessage = new CompletableFuture<>();
        this.stompFrameHandlerMessage = stompFrameHandlerForMessage();
        this.stompFrameHandlerMemberDTO = stompFrameHandlerForMember();
	}
	
	@Test
	public void verifyRegisterToGroup() throws Exception {
		webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
		StompSession session = webSocketStompClient.connect(PATH, new StompSessionHandlerAdapter(){}).get(1, SECONDS);
		session.subscribe("/topic/public", stompFrameHandlerMemberDTO);
		
		Mockito.when(groupService.add(member)).thenReturn(Optional.of(member));
		session.send("/app/register", member);
		MemberDTO response = completableFutureMember.get(10, SECONDS);
        assertNotNull(response);
        assertEquals(member.getUserId(), response.getUserId());
	}
	
	@Test
	@ExceptionHandler(SQLException.class)
	public void notRegisterToGroup() throws SQLException, InterruptedException, ExecutionException, TimeoutException {
		webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
		StompSession session = webSocketStompClient.connect(PATH, new StompSessionHandlerAdapter(){}).get(1, SECONDS);
		session.subscribe("/topic/public", stompFrameHandlerMemberDTO);
		
		Mockito.when(groupService.add(member)).thenThrow(new SQLException());
		session.send("/app/register", member);
	}

	@Test
	public void verifySendAndNotifyMessage() throws Exception {
		webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
		StompSession session = webSocketStompClient.connect(PATH, new StompSessionHandlerAdapter(){}).get(1, SECONDS);
		session.subscribe("/topic/public", stompFrameHandlerMessage);
		
		Mockito.when(messageService.saveMessage(message)).thenReturn(message);
		session.send("/app/send", message);
        Message response = completableFutureMessage.get(10, SECONDS);
        assertNotNull(response);
        assertEquals(message.getMessage(), response.getMessage());
	}
	
	private StompFrameHandler stompFrameHandlerForMember() {
		return new StompFrameHandler() {
			@Override
			public Type getPayloadType(StompHeaders headers) {
				return MemberDTO.class;
			}
			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				completableFutureMember.complete((MemberDTO) payload);
			}
		};
	}
	
	private StompFrameHandler stompFrameHandlerForMessage() {
		return new StompFrameHandler() {
			@Override
			public Type getPayloadType(StompHeaders headers) {
				return Message.class;
			}
			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				completableFutureMessage.complete((Message) payload);
			}
		};
	}
}
