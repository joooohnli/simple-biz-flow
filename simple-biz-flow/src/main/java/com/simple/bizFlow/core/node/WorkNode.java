package com.simple.bizFlow.core.node;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.simple.bizFlow.core.exception.FlowException;
import com.simple.bizFlow.core.utils.SpringUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author joooohnli  2020-01-08 9:06 PM
 */
@XStreamAlias("work-node")
public class WorkNode extends Node implements Serializable {


    @Override
    public void check() {
        if (StringUtils.isEmpty(getBean())) {
            throw new FlowException(String.format("please set bean for work-node(id:%s)", getId()));
        }
        if (SpringUtil.getBean(getBean()) == null) {
            throw new FlowException(String.format("please set valid bean for work-node(id:%s)", getId()));
        }
    }
}
