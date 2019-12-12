package com.misfits.autopilot.controller;

import com.misfits.autopilot.models.EmployeeEntity;
import com.misfits.autopilot.models.EmployeeRepository;
import io.swagger.annotations.Api;
import io.swagger.models.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Api("Autopilot")
@RequestMapping("/hello")
public class AutopilotController {

    @Autowired
    EmployeeRepository repository;

    @RequestMapping(value = "/show/{id}", method= RequestMethod.GET)
    public String sayHello(@PathVariable long id, Model model){
        Optional<EmployeeEntity> emp = repository.findById(id);
        return "Hello "+ emp.get().toString() + "!";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity saveProduct(@RequestBody EmployeeEntity emp){
        repository.save(emp);
        return new ResponseEntity("Employee saved successfully", HttpStatus.OK);
    }

}
