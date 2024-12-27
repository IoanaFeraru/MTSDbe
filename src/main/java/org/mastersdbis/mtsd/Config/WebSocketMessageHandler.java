package org.mastersdbis.mtsd.Config;

import org.mastersdbis.mtsd.Services.MessageService;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;

@Component
public class WebSocketMessageHandler extends TextWebSocketHandler {

    private final MessageService messageService;

    public WebSocketMessageHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message: " + message.getPayload());
        session.sendMessage(new TextMessage("Message received"));
    }
}
