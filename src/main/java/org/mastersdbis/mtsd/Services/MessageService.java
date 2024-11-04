package org.mastersdbis.mtsd.Services;

import org.mastersdbis.mtsd.Entities.Message.Message;
import org.mastersdbis.mtsd.Entities.Message.SenderType;
import org.mastersdbis.mtsd.Entities.Message.SequenceGeneratorService;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    public MessageService(MessageRepository messageRepository, SequenceGeneratorService sequenceGeneratorService) {
        this.messageRepository = messageRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    public Message sendMessage(Message message) {
        if (message.getId() == null) {
            message.setId(sequenceGeneratorService.generateSequence(Message.SEQUENCE_NAME));
            message.setDateSent(LocalDateTime.now());
        }
        try {
            return messageRepository.save(message);
        } catch (Exception e) {
            throw new RuntimeException("Error saving message: " + e.getMessage());
        }
    }

    public void markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() ->
                new RuntimeException("Message not found with id: " + messageId));
        message.setRead(true);
        messageRepository.save(message);
    }

    public Message findById(Long id) {
        return messageRepository.findById(id).orElse(null);
    }

    public List<Message> findByNotificationsAndUser(Integer userId) {
        SenderType type = SenderType.SYSTEM;
        return messageRepository.findBySenderTypeAndSentTo(type, userId);
    }

    public List<Message> findConversation(Integer userId1, Integer userId2) {
        return messageRepository.findByUsersInvolved(userId1, userId2);
    }

    /* ToDO: future - cand persoana intra pe chat sa se apeleze markAsRead
                    - recieve message
                    - recieve notification
                    - implementare exceptii
     */
}
