package com.misfits.autopilot.controller;

import com.misfits.autopilot.convertors.ApiModelBody;
import com.misfits.autopilot.models.entity.*;
import com.misfits.autopilot.models.repositories.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="/workflow", produces ="application/json")
@RequestMapping("/workflow")
public class WorkflowController {

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

    @RequestMapping(value = "/{id}", method= RequestMethod.GET)
    @ApiOperation("Get a workflow")
    public ResponseEntity<Workflow> sayHello(@PathVariable long id, Model model){
        return new ResponseEntity("workflow", HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiOperation("Add a workflow")
    public ResponseEntity<Workflow> saveProduct(@RequestBody ApiModelBody modelBody) {
        save(modelBody);
        return new ResponseEntity("workflow creation successfull", HttpStatus.OK);
    }

    public void save(ApiModelBody modelBody) {
        modelBody.setObjects();
        workflowRepo.save(modelBody.getWorkflow());
//        hookRepository.save(modelBody);
//        criteriaRepository.save(this.criteria);
//        actionRepository.save(this.action);
//        createActionGroup(workflow.getId(), action.getId());
    }

    private ActionGroup createActionGroup(Long workflowId, Long actionId) {
        ActionGroup actGroup = new ActionGroup();
        actGroup.setWorkFlowId(workflowId);
        actGroup.setActionId(actionId);
        actionGroupRepository.save(actGroup);
        return actGroup;
    }
}
