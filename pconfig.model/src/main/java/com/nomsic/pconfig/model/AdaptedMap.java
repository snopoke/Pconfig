package com.nomsic.pconfig.model;

import javax.xml.bind.annotation.XmlAnyElement;

public class AdaptedMap {

    private Object value;

    @XmlAnyElement
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}