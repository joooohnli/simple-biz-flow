package com.simple.bizFlow.core.executor;

import com.simple.bizFlow.api.component.Merge;
import com.simple.bizFlow.api.component.context.FlowContext;
import com.simple.bizFlow.api.component.payload.Payload;
import com.simple.bizFlow.core.constants.FlowConstant;
import com.simple.bizFlow.core.exception.FlowException;
import com.simple.bizFlow.core.node.Flow;
import com.simple.bizFlow.core.node.ParallelNode;
import com.simple.bizFlow.core.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static com.simple.bizFlow.core.constants.FlowConstant.DEFAULT_PARALLEL_TIMEOUT_MILLI_SECONDS;

/**
 * @author joooohnli  2020-03-15 6:33 PM
 */
public class ParallelNodeExecutor implements NodeExecutor<ParallelNode> {
    private static final Logger logger = LoggerFactory.getLogger(ParallelNodeExecutor.class);

    @Override
    public Payload execute(FlowContext context, ParallelNode parallelNode, Payload input) {
        List<Flow> flows = parallelNode.getFlows();

        ExecutorService executorService = SpringUtil.getBean(FlowConstant.PARALLEL_EXECUTOR_SERVICE_BEAN_NAME);
        Map<String, CompletableFuture> map = new HashMap<>();
        for (Flow flow : flows) {
            CompletableFuture future = CompletableFuture.supplyAsync(
                    () -> {
                        return NodeExecutorFactory.execute(context, flow, input);

                    }, executorService);
            map.put(flow.getId(), future);
        }
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(map.values().toArray(new CompletableFuture[0]));
        int timeout = parallelNode.getTimeoutMillSec() == null ? DEFAULT_PARALLEL_TIMEOUT_MILLI_SECONDS : parallelNode.getTimeoutMillSec();
        try {
            voidCompletableFuture.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new FlowException(String.format("parallel-node get error(id:%s)", parallelNode.getId()), e);
        }

        Map<String, Payload> ret = new HashMap<>();
        for (Map.Entry<String, CompletableFuture> entry : map.entrySet()) {
            if (entry.getValue().isDone()) {
                try {
                    ret.put(entry.getKey(), (Payload) entry.getValue().get());
                } catch (InterruptedException | ExecutionException e) {
                    logger.error(String.format("parallel-node flow get error(id:%s)", entry.getKey()), e);
                }
            } else {
                logger.error(String.format("parallel-node flow timeout(id:%s)", entry.getKey()));
            }
        }

        Merge mergeWork = SpringUtil.getBean(parallelNode.getMerge().getBean());
        return mergeWork.merge(context, ret, parallelNode.getPropertiesMap());
    }
}
