# simple-biz-flow
Simple biz flow or data flow for business orchestration

# Get started
find the example in `samples` foler.

## difine components
Work
```
@Service("recall")
public class RecallWork implements Work<MyPayload, MyPayload> {
    @Override
    public MyPayload work(FlowContext context, MyPayload input, Map<String, String> properties) {
        MyFlowContext myFlowContext = (MyFlowContext) context;
        User user = myFlowContext.getUser();
        MyPayload myPayload = new MyPayload();
        // ...
        return myPayload;
    }
}

```
Merge
```
@Service("myMerge")
public class MyMerge implements Merge<MyPayload,MyPayload> {
    @Override
    public MyPayload merge(FlowContext context, Map<String, MyPayload> in, Map<String, String> properties) {
        // ...
        return myPayload;
    }
}
```
Select
```
@Service("mySelect")
public class MySelect implements Select<MyPayload> {

    @Override
    public int select(FlowContext context, MyPayload input, Map<String, String> properties) {
        MyFlowContext myFlowContext = (MyFlowContext) context;
        if (myFlowContext.getStrategy() == 0) {
            return Integer.parseInt(properties.get("default"));
        }

        return 0;
    }
}
```


## config flow
```
<flow-config>
    <flows>
        <flow id="f1">
            <nodes>
                <work-node id="w1" bean="recall"/>
                <parallel-node id="p1" timeoutMillSec="3000">
                    <flows>
                        <flow id="pf1">
                            <nodes>
                                <work-node id="w2" bean="sort1">
                                    <properties>
                                        <property key="x" value="1234"/>
                                    </properties>
                                </work-node>
                            </nodes>
                        </flow>
                        <flow id="pf2">
                            <nodes>
                                <work-node id="w3" bean="sort1">
                                    <properties>
                                        <property key="x" value="5678"/>
                                    </properties>
                                </work-node>
                                <work-node id="w4" bean="sort2"/>
                            </nodes>
                        </flow>
                    </flows>
                    <merge id="m1" bean="myMerge"/>
                </parallel-node>
                <select-node id="s1" bean="mySelect">
                    <properties>
                        <property key="default" value="1"/>
                    </properties>
                    <flows>
                        <flow id="sf1">
                            <nodes>
                                <work-node id="w5" bean="sort2"/>
                            </nodes>
                        </flow>
                        <flow id="sf2"/>
                    </flows>
                </select-node>
            </nodes>
        </flow>
    </flows>
</flow-config>
```

## load flow
```
// load from local file
FlowManager.reloadFromFile("flow.xml", true);
// or load from xml string
FlowManager.reloadFromStr("...", true);
```

## run flow
```
// init your business context
FlowContext flowContext = new MyFlowContext(new User().setMid(0).setLabel("super"), 0);
// start run flow f1
MyPayload execute = FlowManager.execute(flowContext, "f1");
```

## digest log
```
# format: nodeId,elapsed time(ms),Success or Failed|,,|,,
2020-03-27 17:52:58.326  INFO 47400 --- [io-20201-exec-1] com.john.bizflow.digest                  : f1,110,S|w1,0,S|p1,109,S|pf1,1,S|w2,1,S|pf2,104,S|w3,1,S|w4,103,S|s1,0,S|sf2,0,S
```
