package com.simple.bizFlow.api;

import com.thoughtworks.xstream.XStream;
import com.simple.bizFlow.api.component.context.FlowContext;
import com.simple.bizFlow.api.component.payload.Payload;
import com.simple.bizFlow.core.context.SysContext;
import com.simple.bizFlow.core.context.SysContextHolder;
import com.simple.bizFlow.core.exception.FlowException;
import com.simple.bizFlow.core.executor.NodeExecutorFactory;
import com.simple.bizFlow.core.node.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.simple.bizFlow.core.constants.FlowConstant.LOG_DIGEST;

/**
 * biz flow entrance
 * @author joooohnli  2020-01-09 10:25 AM
 */
public class FlowManager {
    private static final Logger logger = LoggerFactory.getLogger(FlowManager.class);
    private static final Logger loggerDigest = LoggerFactory.getLogger(LOG_DIGEST);
    private static final Map<String, Flow> flows = new HashMap<>();
    private static final Class[] TYPES = {FlowConfig.class, Flow.class, Node.class, WorkNode.class, SelectNode.class, ParallelNode.class, ParallelNode.Merge.class};


    /**
     * execute a flow
     * @param context inited business context
     * @param flowId
     * @param <T> output
     * @return
     */
    public static <T extends Payload> T execute(FlowContext context, String flowId) {
        Flow flow = flows.get(flowId);
        if (flow == null) {
            throw new FlowException("unknown flowId:" + flowId);
        }

        T execute;
        try {
            SysContextHolder.setSysContext(new SysContext());

            execute = (T) NodeExecutorFactory.execute(context, flow, null);
        } finally {
            loggerDigest.info("{}", SysContextHolder.getSysContext().toDigest());

            SysContextHolder.clearSysContext();
        }

        return execute;
    }

    /**
     * reload flows from xml file
     * @param xml
     * @param check
     */
    public static void reloadFromStr(String xml, boolean check) {
        logger.info("reloadFromStr start...");
        if (StringUtils.isBlank(xml)) {
            if (check) {
                throw new FlowException("please set flow.xmlString");
            } else {
                return;
            }
        }
        FlowConfig flowConfig = loadFlowConfigFromStr(xml);
        reloadFlow(check, flowConfig);
        logger.info("reloadFromStr end...");
    }

    /**
     * reload flows from string
     * @param filename
     * @param check
     */
    public static void reloadFromFile(String filename, boolean check) {
        logger.info("reloadFromFile start...");
        if (StringUtils.isBlank(filename)) {
            if (check) {
                throw new FlowException("please set flow xml filename");
            } else {
                return;
            }
        }
        FlowConfig flowConfig = loadFlowConfigFromFile(filename);
        reloadFlow(check, flowConfig);
        logger.info("reloadFromFile start...");
    }

    private static void reloadFlow(boolean check, FlowConfig flowConfig) {
        if (check) {
            if (flowConfig == null) {
                throw new FlowException("please set flow config");
            }

            flowConfig.check();
        }

        if (flowConfig.getFlows().size() <= 0) {
            return;
        }

        Map<String, Flow> newFlows = flowConfig.getFlows().stream().collect(Collectors.toMap(Node::getId, f -> f));
        Set<String> newFlowIds = newFlows.keySet();
        List<String> toDeleteKey = flows.keySet().stream().filter(s -> !newFlowIds.contains(s)).collect(Collectors.toList());

        flows.putAll(newFlows);
        toDeleteKey.forEach(flows::remove);

        logger.info(String.format("reload flows count:%s, delete flows count:%s", newFlows.size(), toDeleteKey.size()));
    }


    private static FlowConfig loadFlowConfigFromFile(String fileName) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(fileName);
        XStream xStream = newXStream();
        return (FlowConfig) xStream.fromXML(is);
    }

    private static FlowConfig loadFlowConfigFromStr(String xml) {
        XStream xStream = newXStream();
        return (FlowConfig) xStream.fromXML(xml);
    }


    private static XStream newXStream() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        XStream xStream = new XStream();
        xStream.setClassLoader(classloader);
        xStream.autodetectAnnotations(true);
        xStream.setMode(XStream.NO_REFERENCES);
        xStream.processAnnotations(TYPES);
        return xStream;
    }

}
