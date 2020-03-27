package com.simple.bizFlow.core.executor;

import com.simple.bizFlow.api.component.payload.Payload;
import com.simple.bizFlow.api.component.Work;
import com.simple.bizFlow.api.component.context.FlowContext;
import com.simple.bizFlow.core.node.WorkNode;
import com.simple.bizFlow.core.utils.SpringUtil;

/**
 * @author joooohnli  2020-01-09 10:29 AM
 */
public class WorkNodeExecutor implements NodeExecutor<WorkNode> {

    @Override
    public Payload execute(FlowContext context, WorkNode workNode, Payload input) {
        String bean = workNode.getBean();
        Work work = SpringUtil.getBean(bean);

        return work.work(context, input, workNode.getPropertiesMap());
    }
}
