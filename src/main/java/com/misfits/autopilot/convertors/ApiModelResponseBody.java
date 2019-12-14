package com.misfits.autopilot.convertors;

import com.chargebee.models.enums.EntityType;
import com.chargebee.models.enums.EventType;
import com.misfits.autopilot.models.entity.Action;
import com.misfits.autopilot.models.entity.Criteria;
import com.misfits.autopilot.models.entity.Hook;
import com.misfits.autopilot.models.entity.Workflow;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ApiModelResponseBody {

    private String name;

    private String desc;

    private Workflow.Type type;

    private Workflow.Status status;

    private EntityType entityName;

    private List<EventType> triggers;

    private List<Criteria> criterias;

    private List<Action> actions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Workflow.Type getType() {
        return type;
    }

    public void setType(Workflow.Type type) {
        this.type = type;
    }

    public Workflow.Status getStatus() {
        return status;
    }

    public void setStatus(Workflow.Status status) {
        this.status = status;
    }

    public EntityType getEntityName() {
        return entityName;
    }

    public void setEntityName(EntityType entityName) {
        this.entityName = entityName;
    }

    public List<EventType> getTriggers() {
        return triggers;
    }

//    public void setTriggers(List<EventType> triggers) {
//        this.triggers = triggers;
//    }

    public void setTriggers(List<Hook> hooks) {
        List<EventType> triggerList = new ArrayList<>();
        for (Hook hook : hooks) {
            triggerList.add(hook.getEventType());
        }
        this.triggers = triggerList;
    }

    public List<Criteria> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<Criteria> criterias) {
        this.criterias = criterias;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}
