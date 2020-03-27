package com.simple.bizFlow.core.executor;

import com.simple.bizFlow.api.component.context.FlowContext;
import com.simple.bizFlow.api.component.payload.Payload;
import com.simple.bizFlow.core.context.NodeDigest;
import com.simple.bizFlow.core.context.SysContextHolder;
import com.simple.bizFlow.core.node.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author joooohnli  2020-01-09 10:25 AM
 */
public class NodeExecutorFactory {
    private static final Logger logger = LoggerFactory.getLogger(NodeExecutorFactory.class);

    private static final Map<Class<? extends Node>, NodeExecutor> executorMap = new HashMap<>();

    static {
        executorMap.put(WorkNode.class, new WorkNodeExecutor());
        executorMap.put(SelectNode.class, new SelectNodeExecutor());
        executorMap.put(ParallelNode.class, new ParallelNodeExecutor());
        executorMap.put(Flow.class, new FlowExecutor());
    }

    public static Payload execute(FlowContext context, Node node, Payload input) {
        NodeExecutor nodeExecutor = executorMap.get(node.getClass());
        long start = System.currentTimeMillis();
        NodeDigest nodeDigest = new NodeDigest();
        nodeDigest.setNodeId(node.getId());
        nodeDigest.setBean(node.getBean());
        SysContextHolder.getSysContext().getNodeDigests().add(nodeDigest);

        Payload payload = null;
        try {
            payload = nodeExecutor.execute(context, node, input);
        } catch (Exception e) {
            nodeDigest.setException(true);
            nodeDigest.setExceptionMsg(e.getMessage());
            nodeDigest.setCost(System.currentTimeMillis() - start);
            throw e;
        } finally {
            nodeDigest.setCost(System.currentTimeMillis() - start);
        }
        return payload;
    }


}
