package com.simple.bizFlow.core.node;

import com.simple.bizFlow.core.exception.FlowException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author joooohnli  2020-01-08 9:03 PM
 */
@XStreamAlias("flow-config")
public class FlowConfig extends Node implements Serializable {

    private List<Flow> flows;

    public List<Flow> getFlows() {
        return flows;
    }

    public FlowConfig setFlows(List<Flow> flows) {
        this.flows = flows;
        return this;
    }

    @Override
    public void check() {
        if (flows == null || flows.size() <= 0) {
            throw new FlowException("empty flow");
        }
        for (Flow flow : flows) {
            flow.check();
        }

        Set<String> allItems = new HashSet<>();
        Set<String> duplicated = flows.stream().map(Node::getId)
                .filter(id -> !allItems.add(id))
                .collect(Collectors.toSet());
        if (duplicated.size() > 0) {
            throw new FlowException(String.format("duplicated ids[%s] for flow-config.flows", duplicated));
        }
    }
}
