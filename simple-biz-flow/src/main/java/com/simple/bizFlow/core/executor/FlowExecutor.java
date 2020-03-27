package com.simple.bizFlow.core.executor;

import com.simple.bizFlow.api.component.context.FlowContext;
import com.simple.bizFlow.api.component.payload.Payload;
import com.simple.bizFlow.core.node.Flow;
import com.simple.bizFlow.core.node.Node;

/**
 * @author joooohnli  2020-03-15 6:33 PM
 */
public class FlowExecutor implements NodeExecutor<Flow> {
    @Override
    public Payload execute(FlowContext context, Flow flow, Payload input) {
        if (flow.getNodes() == null) {
            return input;
        }

        Payload output = null;
        for (Node flowNode : flow.getNodes()) {
            output = NodeExecutorFactory.execute(context, flowNode, input);
            input = output;
        }

        return output;
    }
}
