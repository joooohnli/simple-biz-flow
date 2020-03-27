package com.john.bizflow.samples.components;


import com.john.bizflow.samples.components.pos.MyFlowContext;
import com.john.bizflow.samples.components.pos.MyPayload;
import com.john.bizflow.samples.components.pos.User;
import com.simple.bizFlow.api.component.Work;
import com.simple.bizFlow.api.component.context.FlowContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author joooohnli  2020-03-20 1:59 PM
 */
@Service("recall")
public class RecallWork implements Work<MyPayload, MyPayload> {
    @Override
    public MyPayload work(FlowContext context, MyPayload input, Map<String, String> properties) {

        MyPayload myPayload = new MyPayload();
        ArrayList<User> users = new ArrayList<>();
        myPayload.setUsers(users);

        MyFlowContext myFlowContext = (MyFlowContext) context;
        User user = myFlowContext.getUser();

        users.add(new User().setMid(user.getMid()).setLabel(user.getLabel()));
        if (myFlowContext.getStrategy() == 0) {
            users.add(new User().setMid(1).setLabel("a"));
            users.add(new User().setMid(3).setLabel("c"));
            users.add(new User().setMid(2).setLabel("b"));
        } else {
            users.add(new User().setMid(4).setLabel("f"));
            users.add(new User().setMid(5).setLabel("e"));
            users.add(new User().setMid(6).setLabel("g"));
        }

        System.out.println("recall:" + myPayload);
        return myPayload;
    }
}
