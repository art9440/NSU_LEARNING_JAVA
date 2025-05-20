package org.chat.common.messagesOBJ;

import java.io.Serial;
import java.io.Serializable;

public class Error implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public String message;

    public Error() {}

    public Error(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Error: " + message;
    }
}
