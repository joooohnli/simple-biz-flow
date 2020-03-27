package com.simple.bizFlow.core.node;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * @author joooohnli  2020-03-20 5:42 PM
 */
@XStreamAlias("property")
public class Property implements Serializable {
    @XStreamAsAttribute
    private String key;
    @XStreamAsAttribute
    private String value;
    @XStreamAsAttribute
    private String description;

    public String getKey() {
        return key;
    }

    public Property setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Property setValue(String value) {
        this.value = value;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Property setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
