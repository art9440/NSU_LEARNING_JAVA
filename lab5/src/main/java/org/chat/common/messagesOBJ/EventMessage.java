package org.chat.common.messagesOBJ;

import java.io.Serial;
import java.io.Serializable;

public class EventMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public String message; // строка сообщения

    public EventMessage() {}

    public EventMessage(String message) {
        this.message = message;
    }
}
