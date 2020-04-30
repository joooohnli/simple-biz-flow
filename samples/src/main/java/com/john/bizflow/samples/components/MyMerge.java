package com.john.bizflow.samples.components;

import com.john.bizflow.samples.components.pos.MyPayload;
import com.john.bizflow.samples.components.pos.User;
import com.simple.bizFlow.api.component.Merge;
import com.simple.bizFlow.api.component.context.FlowContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author joooohnli  2020-03-20 4:37 PM
 */
@Service("myMerge")
public class MyMerge implements Merge<MyPayload, MyPayload> {
    @Override
    public MyPayload merge(FlowContext context, Map<String, MyPayload> in, Map<String, String> properties) {
        Collection<MyPayload> values = in.values();

        MyPayload myPayload = new MyPayload();
        List<User> users = new ArrayList<>();
        myPayload.setUsers(users);
        for (MyPayload value : values) {
            users.addAll(value.getUsers());
        }

        System.out.println("merge:" + myPayload);
        return myPayload;
    }


}
