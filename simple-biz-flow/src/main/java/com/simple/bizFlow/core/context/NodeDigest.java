package com.simple.bizFlow.core.context;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author joooohnli  2020-03-23 5:05 PM
 */
public class NodeDigest {
    private String nodeId;
    private String bean;
    private long cost;
    private boolean exception;
    private String exceptionMsg;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public String getNodeId() {
        return nodeId;
    }

    public NodeDigest setNodeId(String nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public String getBean() {
        return bean;
    }

    public NodeDigest setBean(String bean) {
        this.bean = bean;
        return this;
    }

    public long getCost() {
        return cost;
    }

    public NodeDigest setCost(long cost) {
        this.cost = cost;
        return this;
    }

    public boolean isException() {
        return exception;
    }

    public NodeDigest setException(boolean exception) {
        this.exception = exception;
        return this;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public NodeDigest setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
        return this;
    }
}
