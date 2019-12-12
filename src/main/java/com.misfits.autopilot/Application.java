package com.misfits.autopilot;

import com.misfits.autopilot.models.EmployeeEntity;
import com.misfits.autopilot.models.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    EmployeeRepository repository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void run(String... args) throws Exception
    {
        Optional<EmployeeEntity> emp = repository.findById(2L);
        logger.info("Employee id 2 -> {}", emp.get());
    }
}