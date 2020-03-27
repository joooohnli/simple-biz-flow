package com.simple.bizFlow.config.spring;

import com.simple.bizFlow.core.utils.SpringUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.concurrent.ExecutorService;

import static com.simple.bizFlow.core.constants.FlowConstant.PARALLEL_EXECUTOR_SERVICE_BEAN_NAME;

/**
 * @author joooohnli  2020-03-23 3:16 PM
 */
public class BizFlowEnvInitializer implements ApplicationContextAware {

    @Autowired
    @Qualifier(PARALLEL_EXECUTOR_SERVICE_BEAN_NAME)
    private ExecutorService executorService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.setApplicationContext(applicationContext);
    }
}
