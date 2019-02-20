package com.jparestservice.controllers;


import com.jparestservice.entities.Employee;
import com.jparestservice.exceptions.ResourceNotFoundException;
import com.jparestservice.model.ErrorDetails;
import com.jparestservice.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;


    @GetMapping("")
    public List<Employee> listEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable("id") Integer id)
    {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Employee found with id="+id));
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable("id") Integer id, @RequestBody @Valid Employee Employee, BindingResult result)
    {
        if(result.hasErrors()){
            throw new IllegalArgumentException("Invalod Employee data");
        }
        employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Employee found with id="+id));
        return  employeeRepository.save(Employee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable("id") Integer id)
    {
        Employee Employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Employee found with id="+id));
                        //new ResourceNotFoundException("No Employee found with id="+id));
        try {
            employeeRepository.deleteById(Employee.getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee with id="+id+" can't be deleted", e);
            //throw new EmployeeDeletionException("Employee with id="+id+" can't be deleted");
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createEmployee(@RequestBody @Valid Employee employee, BindingResult result)
    {
        if(result.hasErrors()){
            StringBuilder devErrorMsg = new StringBuilder();
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError objectError : allErrors) {
                devErrorMsg.append(objectError.getDefaultMessage()+"\n");
            }
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setErrorCode("ERR-1400");//Business specific error codes
            errorDetails.setErrorMessage("Invalid Employee data received");
            errorDetails.setDevErrorMessage(devErrorMsg.toString());

            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }

        Employee savedEmployee = employeeRepository.save(employee);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("MyResponseHeader", "MyValue");
        return new ResponseEntity<>(savedEmployee, responseHeaders, HttpStatus.CREATED);
    }




}
