package com.john.bizflow.samples.action;


import com.john.bizflow.samples.components.pos.MyFlowContext;
import com.john.bizflow.samples.components.pos.MyPayload;
import com.john.bizflow.samples.components.pos.User;
import com.simple.bizFlow.api.FlowManager;
import com.simple.bizFlow.api.component.context.FlowContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author joooohnli  2020-03-22 8:37 PM
 */
@RestController
public class TestAction {


    /**
     * reload config from local file
     *
     * @return
     */
    @RequestMapping("/test")
    public String test() {
        FlowManager.reloadFromFile("flow.xml", true);
        FlowContext flowContext = new MyFlowContext(new User().setMid(0).setLabel("super"), 0);
        MyPayload execute = FlowManager.execute(flowContext, "f1");
        return String.valueOf(execute);
    }


    /**
     * reload config form config center
     *
     * @return
     */
    @RequestMapping("/test2")
    public String test2() {
        FlowContext flowContext = new MyFlowContext(new User().setMid(0).setLabel("super"), 0);
        MyPayload execute = FlowManager.execute(flowContext, "f1");
        return String.valueOf(execute);
    }
}
