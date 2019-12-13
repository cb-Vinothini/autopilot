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

import java.util.List;

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
    @Autowired
    CriteriaGroupRepository criteriaGroupRepository;

    @RequestMapping(value = "/{id}", method= RequestMethod.GET)
    @ApiOperation("Get a workflow")
    public ResponseEntity<Workflow> sayHello(@PathVariable long id, Model model){
        return new ResponseEntity("workflow", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("Add a workflow")
    public ResponseEntity<ApiModelBody> saveProduct(@RequestBody ApiModelBody modelBody) throws Exception {
        save(modelBody);
        return new ResponseEntity(modelBody, HttpStatus.OK);
    }

    public void save(ApiModelBody modelBody) throws Exception {
        modelBody.setWorkflow();
        Workflow workflow = workflowRepo.save(modelBody.getWorkflow());
        modelBody.convertValues(workflow);
        hookRepository.saveAll(modelBody.getHooks());
        criteriaRepository.saveAll(modelBody.getCriterias());
        actionRepository.save(modelBody.getAction());
        createActionGroup(workflow.getId(), modelBody.getAction().getId());
        createCriteriaGroup(workflow.getId(), modelBody.getCriterias());
    }

    private ActionGroup createActionGroup(Long workflowId, Long actionId) {
        ActionGroup actGroup = new ActionGroup();
        actGroup.setWorkflowId(workflowId);
        actGroup.setActionId(actionId);
        actionGroupRepository.save(actGroup);
        return actGroup;
    }

    private void createCriteriaGroup(Long workflowId, List<Criteria> criterias) {
        criterias.stream().forEach(c -> {
            CriteriaGroup criteriaGroup = new CriteriaGroup();
            criteriaGroup.setWorkflowId(workflowId);
            criteriaGroup.setCriteriaId(c.getId());
            criteriaGroupRepository.save(criteriaGroup);
        });
    }
}
