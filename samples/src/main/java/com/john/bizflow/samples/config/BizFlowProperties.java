package com.john.bizflow.samples.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author joooohnli  2020-03-22 9:39 PM
 */
@ConfigurationProperties(prefix = "flow")
public class BizFlowProperties {
    // one line format xml
    private String xmlString;

    private boolean checkWhenReload = true;

    private ParallelPool parallelPool = new ParallelPool();

    public ParallelPool getParallelPool() {
        return parallelPool;
    }

    public BizFlowProperties setParallelPool(ParallelPool parallelPool) {
        this.parallelPool = parallelPool;
        return this;
    }

    public static class ParallelPool {
        private int coreSize = 10;
        private int maxSize = 100;
        private int queueSize = 1000;
        private int keepAliveMillSec = 60 * 1000;

        public int getCoreSize() {
            return coreSize;
        }

        public ParallelPool setCoreSize(int coreSize) {
            this.coreSize = coreSize;
            return this;
        }

        public int getMaxSize() {
            return maxSize;
        }

        public ParallelPool setMaxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public int getQueueSize() {
            return queueSize;
        }

        public ParallelPool setQueueSize(int queueSize) {
            this.queueSize = queueSize;
            return this;
        }

        public int getKeepAliveMillSec() {
            return keepAliveMillSec;
        }

        public ParallelPool setKeepAliveMillSec(int keepAliveMillSec) {
            this.keepAliveMillSec = keepAliveMillSec;
            return this;
        }
    }

    public String getXmlString() {
        return xmlString;
    }

    public BizFlowProperties setXmlString(String xmlString) {
        this.xmlString = xmlString;
        return this;
    }

    public boolean isCheckWhenReload() {
        return checkWhenReload;
    }

    public BizFlowProperties setCheckWhenReload(boolean checkWhenReload) {
        this.checkWhenReload = checkWhenReload;
        return this;
    }
}
