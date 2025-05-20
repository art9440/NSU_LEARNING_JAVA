package org.chat.common.messagesOBJ;

import java.io.Serial;
import java.io.Serializable;

public class MessageCommand implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public String session;   // id сессии
    public String message;
}
