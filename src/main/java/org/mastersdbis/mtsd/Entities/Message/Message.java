package org.mastersdbis.mtsd.Entities.Message;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "messages")
public class Message {

    @Transient
    public static final String SEQUENCE_NAME = "message_sequence";

    @Id
    @Generated
    private Long id;

    @NotEmpty(message = "Message cant be empty")
    private String content;

    private LocalDateTime dateSent;

    private boolean read = false;

    private SenderType senderType;

    private String sentBy;

    private String sentTo;
}
