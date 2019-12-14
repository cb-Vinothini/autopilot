package com.misfits.autopilot.controller;

import com.chargebee.org.json.JSONException;
import com.chargebee.org.json.JSONObject;
import com.misfits.autopilot.convertors.ApiModelBody;
import com.misfits.autopilot.models.entity.*;
import com.misfits.autopilot.models.repositories.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Workflow> getWorkflow(@PathVariable long id) throws JSONException {
        JSONObject workflow = new JSONObject();
        return new ResponseEntity(workflow.toString(2), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation("Delete a workflow")
    public ResponseEntity<Workflow> deleteWorkflow(@PathVariable long id) {
        Optional<Workflow> wf = workflowRepo.findById(id);
        if(!wf.isPresent()) {
            return new ResponseEntity("Workflow not present", HttpStatus.NOT_FOUND);
        }
        Workflow workflow = wf.get();

        CriteriaGroup findCriteriaGroup = new CriteriaGroup();
        findCriteriaGroup.setWorkflowId(workflow.getId());
        List<CriteriaGroup> criteriaGroups = criteriaGroupRepository.findAll(Example.of(findCriteriaGroup));
        for(CriteriaGroup criteriaGroup : criteriaGroups) {
            Criteria criteria = new Criteria();
            criteriaRepository.delete(criteriaRepository.findById(criteriaGroup.getCriteriaId()).get());
        }

        ActionGroup findActionGroup = new ActionGroup();
        findActionGroup.setWorkflowId(workflow.getId());
        List<ActionGroup> actionGroups = actionGroupRepository.findAll(Example.of(findActionGroup));
        for(ActionGroup actionGroup: actionGroups) {
            Action action = new Action();
            actionRepository.delete(actionRepository.findById(actionGroup.getActionId()).get());
        }

        workflowRepo.delete(workflow);
        return new ResponseEntity("Workflow has been deleted successfully", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("Add a workflow")
    public ResponseEntity<ApiModelBody> saveProduct(@RequestBody ApiModelBody modelBody) throws Exception {
        save(modelBody);
        return new ResponseEntity("Workflow has been created successfully", HttpStatus.OK);
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
