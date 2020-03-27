package com.simple.bizFlow.api.component;

import com.simple.bizFlow.api.component.context.FlowContext;
import com.simple.bizFlow.api.component.payload.Payload;

import java.util.Map;

/**
 * select component of select-node
 *
 * @author joooohnli  2020-03-18 5:37 PM
 */
public interface Select<T extends Payload> {
    /**
     *
     *
     * @param context
     * @param input
     * @param properties
     * @return index of subflows (from 0)
     */
    int select(FlowContext context, T input, Map<String, String> properties);
}
