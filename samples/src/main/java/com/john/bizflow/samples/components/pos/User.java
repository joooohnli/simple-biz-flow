package com.john.bizflow.samples.components.pos;

import java.util.StringJoiner;

/**
 * @author joooohnli  2020-03-20 2:02 PM
 */
public class User {
    private int mid;
    private String label;

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("mid=" + mid)
                .add("label='" + label + "'")
                .toString();
    }

    public int getMid() {
        return mid;
    }

    public User setMid(int mid) {
        this.mid = mid;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public User setLabel(String label) {
        this.label = label;
        return this;
    }
}
