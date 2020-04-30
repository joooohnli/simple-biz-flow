package com.john.bizflow.samples.components;

import com.john.bizflow.samples.components.pos.MyPayload;
import com.john.bizflow.samples.components.pos.User;
import com.simple.bizFlow.api.component.Work;
import com.simple.bizFlow.api.component.context.FlowContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author joooohnli  2020-03-20 1:59 PM
 */
@Service("sort1")
public class SortWork implements Work<MyPayload, MyPayload> {
    @Override
    public MyPayload work(FlowContext context, MyPayload input, Map<String, String> properties) {
        List<User> users = new ArrayList<>(input.getUsers());

        users.sort(Comparator.comparingInt(User::getMid));

        String s = properties.get("x");


        MyPayload myPayload = new MyPayload();
        myPayload.setUsers(users);
        System.out.println("sort1:" + myPayload);
        return myPayload;
    }
}
