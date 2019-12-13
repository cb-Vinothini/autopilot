package com.misfits.autopilot.models.entity;

import com.chargebee.models.enums.EventType;

import java.io.Serializable;

public class HookCompositeKey implements Serializable {

    private EventType eventType;

    private Long workflowId;

}
