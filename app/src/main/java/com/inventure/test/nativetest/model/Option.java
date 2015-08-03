package com.inventure.test.nativetest.model;

/**
 * Created by Anand Chokshi on 8/3/2015.
 */
public class Option {
    private String value;
    private String label;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
