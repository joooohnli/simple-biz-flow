package com.simple.bizFlow.core.executor;

import com.simple.bizFlow.api.component.Select;
import com.simple.bizFlow.api.component.context.FlowContext;
import com.simple.bizFlow.api.component.payload.Payload;
import com.simple.bizFlow.core.node.Flow;
import com.simple.bizFlow.core.node.SelectNode;
import com.simple.bizFlow.core.utils.SpringUtil;

import java.util.List;

/**
 * @author joooohnli  2020-03-15 6:33 PM
 */
public class SelectNodeExecutor implements NodeExecutor<SelectNode> {
    @Override
    public Payload execute(FlowContext context, SelectNode selectNode, Payload input) {
        String mode = selectNode.getMode();
        String bean = selectNode.getBean();
        String script = selectNode.getScript();
        List<Flow> flows = selectNode.getFlows();

        Select select = SpringUtil.getBean(bean);
        int select1 = select.select(context, input, selectNode.getPropertiesMap());
        Flow flow = flows.get(select1);

        if (SelectNode.MODE_SCRIPT.equalsIgnoreCase(mode)) {
            //todo script
        }

        return NodeExecutorFactory.execute(context, flow, input);
    }
}
