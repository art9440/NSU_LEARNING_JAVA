package org.chat.common.messagesOBJ;

import java.io.Serial;
import java.io.Serializable;

public class Success implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public String sessionId; // может быть null

    public Success() {}

    public Success(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return sessionId != null
                ? "Success: sessionId = " + sessionId
                : "Success";
    }
}

