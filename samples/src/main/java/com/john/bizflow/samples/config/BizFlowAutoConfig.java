package com.john.bizflow.samples.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.simple.bizFlow.api.FlowManager;
import com.simple.bizFlow.config.spring.BizFlowEnvInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.simple.bizFlow.core.constants.FlowConstant.PARALLEL_EXECUTOR_SERVICE_BEAN_NAME;

/**
 * @author joooohnli  2020-03-16 5:17 PM
 */
@Configuration
@EnableConfigurationProperties(value = {BizFlowProperties.class})
public class BizFlowAutoConfig {
    @Autowired
    private BizFlowProperties bizFlowProperties;
    private AtomicBoolean xmlInited = new AtomicBoolean(false);

    @Bean
    public BizFlowEnvInitializer bizFlowEnvInitializer() {
        return new BizFlowEnvInitializer();
    }

    @Bean(name = PARALLEL_EXECUTOR_SERVICE_BEAN_NAME)
    public ExecutorService getExecutorService() {
        BizFlowProperties.ParallelPool parallelPool = bizFlowProperties.getParallelPool();
        return new ThreadPoolExecutor(parallelPool.getCoreSize(), parallelPool.getMaxSize(),
                parallelPool.getKeepAliveMillSec(), TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(parallelPool.getQueueSize()),
                new ThreadFactoryBuilder().setDaemon(true).setNameFormat("flow-parallel-%d").build()
        );
    }

    @EventListener
    public void eventListener(ContextRefreshedEvent event) {
        if (xmlInited.compareAndSet(false, true)) {
            FlowManager.reloadFromStr(bizFlowProperties.getXmlString(), bizFlowProperties.isCheckWhenReload());
        }
    }

    @EventListener
    public void eventListener(RefreshScopeRefreshedEvent event) {
        FlowManager.reloadFromStr(bizFlowProperties.getXmlString(), bizFlowProperties.isCheckWhenReload());
    }

}
