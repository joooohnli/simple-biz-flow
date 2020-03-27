package com.john.bizflow.samples.components;

import com.john.bizflow.samples.components.pos.MyFlowContext;
import com.john.bizflow.samples.components.pos.MyPayload;
import com.simple.bizFlow.api.component.Select;
import com.simple.bizFlow.api.component.context.FlowContext;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * @author joooohnli  2020-03-20 4:33 PM
 */
@Service("mySelect")
public class MySelect implements Select<MyPayload> {

    @Override
    public int select(FlowContext context, MyPayload input, Map<String, String> properties) {
        MyFlowContext myFlowContext = (MyFlowContext) context;

        if (myFlowContext.getStrategy() == 0) {
            return Integer.parseInt(properties.get("default"));
        }

        return 0;
    }
}
