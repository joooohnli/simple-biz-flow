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
@XStreamAlias("select-node")
public class SelectNode extends Node implements Serializable {
    public static final String MODE_BEAN = "bean";
    public static final String MODE_SCRIPT = "script";

    private List<Flow> flows;

    @XStreamAsAttribute
    private String mode;
    @XStreamAsAttribute
    private String script;

    public String getMode() {
        return mode;
    }

    public SelectNode setMode(String mode) {
        this.mode = mode;
        return this;
    }

    public List<Flow> getFlows() {
        return flows;
    }

    public SelectNode setFlows(List<Flow> flows) {
        this.flows = flows;
        return this;
    }


    public String getScript() {
        return script;
    }

    public SelectNode setScript(String script) {
        this.script = script;
        return this;
    }

    @Override
    public void check() {
        if (StringUtils.isBlank(getBean())) {
            throw new FlowException(String.format("please set bean for select-node(id:%s)", getId()));
        }
        if (SpringUtil.getBean(getBean()) == null) {
            throw new FlowException(String.format("please set valid bean for select-node(id:%s)", getId()));
        }

        if (flows == null || flows.size() <= 0) {
            throw new FlowException(String.format("please set flows for select-node(id:%s)", getId()));
        }
        for (Flow flow : flows) {
            flow.check();
        }

        Set<String> allItems = new HashSet<>();
        Set<String> duplicated = flows.stream().map(Node::getId)
                .filter(id -> !allItems.add(id))
                .collect(Collectors.toSet());
        if (duplicated.size() > 0) {
            throw new FlowException(String.format("duplicated ids[%s] for select-node.flows[id:%s]", duplicated, getId()));
        }
    }
}
