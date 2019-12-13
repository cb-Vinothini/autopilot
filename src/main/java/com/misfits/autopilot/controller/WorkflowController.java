package com.misfits.autopilot.controller;

import com.misfits.autopilot.convertors.ApiModelBody;
import com.misfits.autopilot.models.entity.Workflow;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="/workflow", produces ="application/json")
@RequestMapping("/workflow")
public class WorkflowController {

    @RequestMapping(value = "/{id}", method= RequestMethod.GET)
    @ApiOperation("Get a workflow")
    public ResponseEntity<Workflow> sayHello(@PathVariable long id, Model model){
        return new ResponseEntity("workflow", HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiOperation("Add a workflow")
    public ResponseEntity<Workflow> saveProduct(@RequestBody ApiModelBody modelBody) {
        modelBody.save();
        return new ResponseEntity("workflow creation successfull", HttpStatus.OK);
    }
}
