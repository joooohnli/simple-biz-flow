package com.john.bizflow.samples.components.pos;

import com.simple.bizFlow.api.component.context.FlowContext;

/**
 * @author joooohnli  2020-03-20 2:02 PM
 */
public class MyFlowContext implements FlowContext {
    private final User user;
    private final int strategy;
    private String trace;

    public MyFlowContext(User user, int strategy) {
        this.user = user;
        this.strategy = strategy;
    }

    public User getUser() {
        return user;
    }

    public int getStrategy() {
        return strategy;
    }

    public String getTrace() {
        return trace;
    }

    public MyFlowContext setTrace(String trace) {
        this.trace = trace;
        return this;
    }
}
