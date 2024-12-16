package webSocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import webSocket.model.Message;

@Controller
public class WebSocketController {	
	
	@MessageMapping("/logs")
	@SendTo("/topic/log")
    public Message getFileUpdates(Message message) {
        return message;
    }
}