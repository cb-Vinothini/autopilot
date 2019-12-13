package com.misfits.autopilot.models.entity;

import io.swagger.annotations.ApiModelProperty;

public class Attribute {

    @ApiModelProperty(example = "amount")
    private String name;

    @ApiModelProperty(example = "300")
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "\""+name + "\":\"" + value+"\"";
    }
}