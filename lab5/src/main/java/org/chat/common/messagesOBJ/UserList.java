package org.chat.common.messagesOBJ;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class UserList implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public List<String> users;

    public UserList() {}

    public UserList(List<String> users) {
        this.users = users;
    }
}
