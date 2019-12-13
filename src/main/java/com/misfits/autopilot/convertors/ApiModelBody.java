package com.misfits.autopilot.convertors;

import com.misfits.autopilot.models.entity.*;
import com.misfits.autopilot.models.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;

public class ApiModelBody {

    @Autowired
    private Workflow workflow;
    @Autowired
    private Hook hook;
    @Autowired
    private Action action;
    @Autowired
    private Criteria criteria;
    @Autowired
    private ActionGroup actionGroup;

    @Autowired
    WorkflowRepository workflowRepo;
    @Autowired
    HookRepository hookRepository;
    @Autowired
    ActionRepository actionRepository;
    @Autowired
    CriteriaRepository criteriaRepository;
    @Autowired
    ActionGroupRepository actionGroupRepository;


    /*
    *   Getters
    */

    public Workflow getWorkflow() {
        return workflow;
    }

    public Hook getHook() {
        return hook;
    }

    public Criteria getCriteria() {
        return criteria;
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

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    /*
    *   Save
    */

    public void save() {
        this.workflow = workflowRepo.save(this.workflow);
        this.hook = hookRepository.save(this.hook);
        this.criteria = criteriaRepository.save(this.criteria);
        this.action = actionRepository.save(this.action);
        this.actionGroup = createActionGroup(workflow.getId(), action.getId());

    }

    private ActionGroup createActionGroup(Long workflowId, Long actionId) {
        ActionGroup actGroup = new ActionGroup();
        actGroup.setWorkFlowId(workflowId);
        actGroup.setActionId(actionId);
        actionGroupRepository.save(actGroup);
        return actGroup;
    }
}

