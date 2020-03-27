package com.simple.bizFlow.core.utils;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

/**
 * @author joooohnli  2020-03-16 5:29 PM
 */
public class SpringUtil {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringUtil.applicationContext = applicationContext;
    }


    public static <T> T getBean(String name) {
        try {
            return (T) applicationContext.getBean(name);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    public static <T> T getBean(Class<T> clazz) {

        String[] beanNames = applicationContext.getBeanNamesForType(clazz);
        if (beanNames.length == 1) {
            return (T) applicationContext.getBean(beanNames[0]);
        } else {
            return null;
        }
    }

}
