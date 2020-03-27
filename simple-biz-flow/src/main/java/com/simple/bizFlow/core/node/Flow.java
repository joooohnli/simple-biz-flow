package com.simple.bizFlow.core.node;

import com.simple.bizFlow.core.exception.FlowException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author joooohnli  2020-01-08 9:03 PM
 */
@XStreamAlias("flow")
public class Flow extends Node implements Serializable {

    private List<Node> nodes;

    public List<Node> getNodes() {
        return nodes;
    }

    public Flow setNodes(List<Node> nodes) {
        this.nodes = nodes;
        return this;
    }

    @Override
    public void check() {
        if (StringUtils.isBlank(getId())) {
            throw new FlowException(String.format("please set id for (%s)", this));
        }
        if (nodes != null && nodes.size() > 0) {
            for (Node node : nodes) {
                node.check();
            }
        }
    }
}
