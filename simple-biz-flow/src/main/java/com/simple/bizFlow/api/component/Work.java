package com.simple.bizFlow.api.component;

import com.simple.bizFlow.api.component.context.FlowContext;
import com.simple.bizFlow.api.component.payload.Payload;

import java.util.Map;

/**
 * work component of work-node
 *
 * @author joooohnli  2020-01-09 10:18 AM
 */
public interface Work<IN extends Payload, OUT extends Payload> {

    /**
     * @param context
     * @param input       <font color="red">modifying content of input may lead to unpredicted results</font>
     * @param properties
     * @return output
     */
    OUT work(FlowContext context, IN input, Map<String, String> properties);
}
