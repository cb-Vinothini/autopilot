package com.misfits.autopilot.convertors;

import com.chargebee.models.enums.EntityType;
import com.chargebee.models.enums.EventType;
import com.misfits.autopilot.models.entity.*;
import org.hibernate.jdbc.Work;

import java.util.List;

public class ApiModelBody {

    private String name;
    private String desc;
    private Workflow.WorkflowType workflowType;
    private EntityType entityName;
    private EventType trigger;

    private List<Criteria> criterias;
    private ActionGroup actionGroup;
    public Workflow workflow;
    private Hook hook;
    private Action action;

    /*
    *   Getters
    */

    public Workflow getWorkflow() {
        return workflow;
    }

    public Hook getHook() {
        return hook;
    }


    public Action getAction() {
        return action;
    }

    /*
     *  Setters
     */

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    public void setHook(Hook hook) {
        this.hook = hook;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public List<Criteria> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<Criteria> criterias) {
        this.criterias = criterias;
    }

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

    public Workflow.WorkflowType getWorkflowType() {
        return workflowType;
    }

    public void setWorkflowType(Workflow.WorkflowType workflowType) {
        this.workflowType = workflowType;
    }


    public EntityType getEntityName() {
        return entityName;
    }

    public void setEntityName(EntityType entityName) {
        this.entityName = entityName;
    }

    public void setWorkflow() {
        workflow = new Workflow();
        workflow.setName(name);
        workflow.setDescription(desc);
        workflow.setEntityType(entityName);
        workflow.setType(workflowType);
    }

    public void setObjs(Workflow flow) {
        hook = new Hook();
        hook.setEventType(trigger);
        hook.setWorkflowId(flow.getId());
    }

    public EventType getTrigger() {
        return trigger;
    }

    public void setTrigger(EventType trigger) {
        this.trigger = trigger;
    }
}

