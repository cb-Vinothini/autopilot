package com.misfits.autopilot.controller;

import com.misfits.autopilot.models.EmployeeEntity;
import com.misfits.autopilot.models.EmployeeRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Api(value="/hello", produces ="application/json")
@RequestMapping("/hello")
public class AutopilotController {

    @Autowired
    EmployeeRepository repository;

    @RequestMapping(value = "/show/{id}", method= RequestMethod.GET)
    @ApiOperation("Fetch specific employee")
    public ResponseEntity<EmployeeEntity> sayHello(@PathVariable long id, Model model){
        Optional<EmployeeEntity> emp = repository.findById(id);
        return new ResponseEntity(emp, HttpStatus.OK);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("Add a employee")
    public ResponseEntity<EmployeeEntity> saveProduct(@RequestBody EmployeeEntity emp){
        repository.save(emp);
        return new ResponseEntity("Employee saved successfully", HttpStatus.OK);
    }

}
