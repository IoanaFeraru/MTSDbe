package org.mastersdbis.mtsd.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mastersdbis.mtsd.Entities.Message.Message;
import org.mastersdbis.mtsd.Entities.Message.MessageType;
import org.mastersdbis.mtsd.Entities.Service.Service;
import org.mastersdbis.mtsd.Entities.User.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private Integer id;
    private String message;
    private int senderId;
    private int receiverId;
    private String fileUrl;
    private MessageType messageType;
    private Integer serviceId;

    public Message toMessage(User sender, User receiver, Service service) {
        Message message = new Message();
        message.setId(id);
        message.setMessage(this.message);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setFileUrl(fileUrl);
        message.setMessageType(messageType);
        message.setService(service);
        return message;
    }

    public static MessageDTO fromMessage(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getMessage(),
                message.getSender().getId(),
                message.getReceiver().getId(),
                message.getFileUrl(),
                message.getMessageType(),
                message.getService() != null ? message.getService().getId() : null
        );
    }
}
