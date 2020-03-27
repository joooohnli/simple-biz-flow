package com.simple.bizFlow.core.executor;

import com.simple.bizFlow.api.component.payload.Payload;
import com.simple.bizFlow.api.component.context.FlowContext;
import com.simple.bizFlow.core.node.Node;

/**
 * @author joooohnli  2020-01-09 10:20 AM
 */
public interface NodeExecutor<T extends Node> {

    Payload execute(FlowContext context, T node, Payload input);

}
