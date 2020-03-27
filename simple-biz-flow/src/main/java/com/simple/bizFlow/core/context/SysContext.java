package com.simple.bizFlow.core.context;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author joooohnli  2020-03-23 5:04 PM
 */
public class SysContext {
    private List<NodeDigest> nodeDigests = new ArrayList<>();

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public String toDigest() {
        return nodeDigests.stream().map(n -> String.format("%s,%s,%s", n.getNodeId() == null ? n.getBean() : n.getNodeId(), n.getCost(), n.isException()?"E":"S")).collect(Collectors.joining("|"));
    }

    public List<NodeDigest> getNodeDigests() {
        return nodeDigests;
    }

    public SysContext setNodeDigests(List<NodeDigest> nodeDigests) {
        this.nodeDigests = nodeDigests;
        return this;
    }
}
