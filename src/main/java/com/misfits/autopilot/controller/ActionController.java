package com.misfits.autopilot.controller;

import com.misfits.autopilot.models.entity.Action;
import com.misfits.autopilot.models.repositories.ActionRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Api(value="/action", produces ="application/json")
@RequestMapping("/action")
public class ActionController {

    private String notFound = "action not found";

    @Autowired
    ActionRepository actionRespositorty;

    @RequestMapping(value = "/{id}", method= RequestMethod.POST)
    @ApiOperation("Update an action")
    public ResponseEntity<Action> updateAction(@PathVariable long id, Model model, @RequestBody Action act){
        Optional<Action> action = actionRespositorty.findById(id);
        if(action.isPresent()) {
            Action actObj = action.get();
            actObj.setApiName(act.getApiName());
            actObj.setApiParameters(act.getApiParameters());
            actionRespositorty.save(actObj);
            return new ResponseEntity(actObj, HttpStatus.OK);
        } else {
            return new ResponseEntity(notFound, HttpStatus.NO_CONTENT);
        }
    }




}
