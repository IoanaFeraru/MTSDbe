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
    private Integer id;

    @NotNull(message = "Message content cannot be null.")
    @Size(max = 1000, message = "Message content cannot exceed 1000 characters.")
    private String message;

    @NotNull(message = "Sender user ID cannot be null.")
    private Integer senderUserId;

    @NotNull(message = "Receiver user ID cannot be null.")
    private Integer receiverUserId;

    private String fileUrl;

    private MessageType messageType;
}