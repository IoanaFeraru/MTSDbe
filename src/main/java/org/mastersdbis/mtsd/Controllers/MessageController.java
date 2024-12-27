package org.mastersdbis.mtsd.Controllers;

import org.mastersdbis.mtsd.DTO.MessageDTO;
import org.mastersdbis.mtsd.Entities.Message.Message;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Services.MessageService;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Services.ServiceService;
import org.mastersdbis.mtsd.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final ServiceService serviceService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService, ServiceService serviceService) {
        this.messageService = messageService;
        this.userService = userService;
        this.serviceService = serviceService;
    }

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody MessageDTO messageDTO) {
        User sender = userService.findById(messageDTO.getSenderId());
        User receiver = userService.findById(messageDTO.getReceiverId());
        Service service = serviceService.findById(messageDTO.getServiceId());

        Message message = messageDTO.toMessage(sender, receiver, service);
        messageService.sendMessage(message);
        return ResponseEntity.ok("Message sent successfully.");
    }

    @GetMapping("/between/{senderId}/{receiverId}")
    public ResponseEntity<List<MessageDTO>> getMessagesBetweenUsers(@PathVariable Integer senderId, @PathVariable Integer receiverId) {
        User sender = new User();
        sender.setId(senderId);
        User receiver = new User();
        receiver.setId(receiverId);
        List<Message> messages = messageService.getMessagesBetweenUsers(sender, receiver);
        List<MessageDTO> messageDTOs = messages.stream()
                .map(MessageDTO::fromMessage)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageDTOs);
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<MessageDTO>> getMessagesBySender(@PathVariable Integer senderId) {
        User sender = new User();
        sender.setId(senderId);
        List<Message> messages = messageService.getMessagesBySender(sender);
        List<MessageDTO> messageDTOs = messages.stream()
                .map(MessageDTO::fromMessage)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageDTOs);
    }

    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<List<MessageDTO>> getMessagesByReceiver(@PathVariable Integer receiverId) {
        User receiver = new User();
        receiver.setId(receiverId);
        List<Message> messages = messageService.getMessagesByReceiver(receiver);
        List<MessageDTO> messageDTOs = messages.stream()
                .map(MessageDTO::fromMessage)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageDTOs);
    }
}
