package com.john.bizflow.samples;

import com.simple.bizFlow.api.FlowManager;
import com.simple.bizFlow.utils.GraphUtil;

/**
 * @author joooohnli  2020-04-30 6:32 PM
 **/
public class Test {
    public static void main(String[] args) {
        FlowManager.reloadFromFile("flow2.xml", false);
        GraphUtil.drawGraph(FlowManager.getFlow("flow"), "/Users/john/Downloads/flow.svg");
    }
}
