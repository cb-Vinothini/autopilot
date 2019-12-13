package com.misfits.autopilot.convertors;

import com.chargebee.models.enums.EntityType;
import com.chargebee.models.enums.EventType;
import com.misfits.autopilot.models.entity.*;
import io.swagger.annotations.ApiModelProperty;

import java.util.LinkedList;
import java.util.List;

public class ApiModelBody {

    @ApiModelProperty(example = "Add shipping charges")
    private String name;

    @ApiModelProperty(example = "Add shipping charges when subscription shipping address is in US")
    private String desc;

    private Workflow.WorkflowType workflowType;

    @ApiModelProperty(example = "SUBSCRIPTION")
    private EntityType entityName;

    @ApiModelProperty(example = "SUBSCRIPTION_CREATED")
    private List<EventType> triggers;

    private List<Criteria> criterias;
    private ActionGroup actionGroup;
    public Workflow workflow;
    private List<Hook> hooks = new LinkedList<>();
    private Action action;

    /*
    *   Getters
    */

    public Workflow getWorkflow() {
        return workflow;
    }

    public List<Hook> getHooks() {
        return hooks;
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

    public void setHooks(List<Hook> hooks) {
        this.hooks = hooks;
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

    public void convertValues(Workflow flow) throws Exception {
        triggers.forEach(t -> {
            Hook hook = new Hook();
            hook.setEventType(t);
            hook.setWorkflowId(flow.getId());
            hooks.add(hook);
        });
        for(Criteria criteria : criterias) {
            criteria.convertValues();
        }
        action.convertValues();
    }

    public List<EventType> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<EventType> triggers) {
        this.triggers = triggers;
    }
}

