package com.simple.bizFlow.api.component;

import com.simple.bizFlow.api.component.context.FlowContext;
import com.simple.bizFlow.api.component.payload.Payload;

import java.util.Map;

/**
 * merge component of parallel-node to merge outputs
 *
 * @author joooohnli  2020-03-15 11:12 PM
 */
public interface Merge<IN extends Payload, OUT extends Payload> {

    /**
     * @param context
     * @param in         key:flowId,value:payloads of flows
     * @param properties
     * @return
     */
    OUT merge(FlowContext context, Map<String, IN> in, Map<String, String> properties);
}
