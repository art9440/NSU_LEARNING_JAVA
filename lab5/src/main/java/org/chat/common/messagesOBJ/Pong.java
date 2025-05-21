package org.chat.common.messagesOBJ;

import java.io.Serial;
import java.io.Serializable;

public class Pong implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public String session;

    public Pong() {}

    public Pong(String session) {
        this.session = session;
    }

}
