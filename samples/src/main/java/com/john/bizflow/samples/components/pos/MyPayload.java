package com.john.bizflow.samples.components.pos;




import com.simple.bizFlow.api.component.payload.Payload;

import java.util.List;
import java.util.StringJoiner;

/**
 * @author joooohnli  2020-03-20 4:13 PM
 */
public class MyPayload implements Payload {
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public MyPayload setUsers(List<User> users) {
        this.users = users;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MyPayload.class.getSimpleName() + "[", "]")
                .add("users=" + users)
                .toString();
    }
}
