package com.simple.bizFlow.core.node;

import com.simple.bizFlow.core.exception.FlowException;
import com.simple.bizFlow.core.utils.SpringUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author joooohnli  2020-03-15 6:01 PM
 */
@XStreamAlias("parallel-node")
public class ParallelNode extends Node implements Serializable {
    @XStreamAsAttribute
    private Integer timeoutMillSec;
    private List<Flow> flows;

    private Merge merge;

    public static class Merge extends Node {


        @Override
        public void check() {
            if (StringUtils.isBlank(getBean())) {
                throw new FlowException(String.format("please set merge bean for parallel-node(id:%s)", getId()));
            }
            if (SpringUtil.getBean(getBean()) == null) {
                throw new FlowException(String.format("please set valid merge bean for parallel-node(id:%s)", getId()));
            }
        }
    }

    @Override
    public void check() {
        if (flows == null || flows.size() <= 0) {
            throw new FlowException(String.format("please set flows for parallel-node(id:%s)", getId()));
        }
        for (Flow flow : flows) {
            flow.check();
        }
        Set<String> allItems = new HashSet<>();
        Set<String> duplicated = flows.stream().map(Node::getId)
                .filter(id -> !allItems.add(id))
                .collect(Collectors.toSet());
        if (duplicated.size() > 0) {
            throw new FlowException(String.format("duplicated ids[%s] for parallel-node.flows[id:%s]", duplicated, getId()));
        }


        if (merge == null) {
            throw new FlowException(String.format("please set merge for parallel-node(id:%s)", getId()));
        }
        merge.check();
    }

    public List<Flow> getFlows() {
        return flows;
    }

    public ParallelNode setFlows(List<Flow> flows) {
        this.flows = flows;
        return this;
    }

    public Merge getMerge() {
        return merge;
    }

    public ParallelNode setMerge(Merge merge) {
        this.merge = merge;
        return this;
    }

    public Integer getTimeoutMillSec() {
        return timeoutMillSec;
    }

    public ParallelNode setTimeoutMillSec(Integer timeoutMillSec) {
        this.timeoutMillSec = timeoutMillSec;
        return this;
    }
}
