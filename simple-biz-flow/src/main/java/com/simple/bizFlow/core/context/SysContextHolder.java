package com.simple.bizFlow.core.context;

/**
 * @author joooohnli  2020-03-23 5:07 PM
 */
public class SysContextHolder {
    private static ThreadLocal<SysContext> sysContextThreadLocal = new InheritableThreadLocal<>();

    public static void setSysContext(SysContext sysContext) {
        sysContextThreadLocal.set(sysContext);
    }

    public static SysContext getSysContext() {
        return sysContextThreadLocal.get();
    }

    public static void clearSysContext() {
        sysContextThreadLocal.remove();
    }
}
