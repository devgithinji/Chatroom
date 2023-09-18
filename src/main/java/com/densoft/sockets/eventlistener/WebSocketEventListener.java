package com.densoft.sockets.eventlistener;

import com.densoft.sockets.model.ChatMessage;
import com.densoft.sockets.model.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageSendingOperations;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent sessionDisconnectEvent) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {

            log.info("user disconnected: {} ", username);
            ChatMessage chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .build();

            messageSendingOperations.convertAndSend("/topic/public", chatMessage);
        }
    }
}
