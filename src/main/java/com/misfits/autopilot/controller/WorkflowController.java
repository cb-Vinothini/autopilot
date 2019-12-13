package com.misfits.autopilot.controller;

import com.misfits.autopilot.models.Workflow;
import com.misfits.autopilot.models.WorkflowRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Api(value="/workflow", produces ="application/json")
@RequestMapping("/workflow")
public class WorkflowController {

    @Autowired
    WorkflowRepository repository;

    @RequestMapping(value = "/{id}", method= RequestMethod.GET)
    @ApiOperation("Get a workflow")
    public ResponseEntity<Workflow> sayHello(@PathVariable long id, Model model){
        Optional<Workflow> workflow = repository.findById(id);
        return new ResponseEntity(workflow, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation("Add a workflow")
    public ResponseEntity<Workflow> saveProduct(@RequestBody Workflow workflow){
        Workflow wf = repository.save(workflow);
        return new ResponseEntity(wf, HttpStatus.OK);
    }
}
