package com.simple.bizFlow.core.node;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author joooohnli  2020-01-08 9:06 PM
 */
public abstract class Node implements Serializable {
    @XStreamAsAttribute
    private String id;
    @XStreamAsAttribute
    private String bean;
    @XStreamAsAttribute
    private String description;
    private List<Property> properties;


    private transient Map<String, String> propertiesMap;

    public Map<String, String> getPropertiesMap() {
        if (propertiesMap == null) {
            propertiesMap = new HashMap<>();
            List<Property> properties = getProperties();
            if (properties == null) {
                return propertiesMap;
            }
            for (Property property : properties) {
                propertiesMap.put(property.getKey(), property.getValue());
            }
        }
        return propertiesMap;
    }


    public abstract void check();

    public String getId() {
        return id;
    }

    public Node setId(String id) {
        this.id = id;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Node setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public Node setProperties(List<Property> properties) {
        this.properties = properties;
        return this;
    }

    public String getBean() {
        return bean;
    }

    public Node setBean(String bean) {
        this.bean = bean;
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
