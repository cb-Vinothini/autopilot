package com.misfits.autopilot.controller;

import com.misfits.autopilot.models.EmployeeEntity;
import com.misfits.autopilot.models.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AutopilotController {

    @Autowired
    EmployeeRepository repository;

    @RequestMapping("hello")
    public String sayHello(@RequestParam(value = "id") long id){
        Optional<EmployeeEntity> emp = repository.findById(id);
        return "Hello "+ emp.toString() + "!";
    }
}
