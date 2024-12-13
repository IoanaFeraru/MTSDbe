package org.mastersdbis.mtsd.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mastersdbis.mtsd.Entities.Message.MessageType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private Integer id; // Unique identifier for the message

    @NotNull(message = "Message content cannot be null.")
    @Size(max = 1000, message = "Message content cannot exceed 1000 characters.")
    private String message; // The message content

    @NotNull(message = "Sender user ID cannot be null.")
    private Integer senderUserId; // ID of the sender

    @NotNull(message = "Receiver user ID cannot be null.")
    private Integer receiverUserId; // ID of the receiver

    private String fileUrl; // Optional field for file attachment URL

    private MessageType messageType; // Enum for the type of the message
}