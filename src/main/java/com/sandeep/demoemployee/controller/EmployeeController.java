package com.sandeep.demoemployee.controller;

//import com.sandeep.demoemployee.entity.Employee;
import com.sandeep.demoemployee.entity.Employee;
import com.sandeep.demoemployee.service.EmployeeService;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PessimisticLockScope;
import javax.validation.constraints.Null;
import javax.xml.ws.Response;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/employee")
public class EmployeeController
{
    @Autowired
    private EmployeeService employeeService;
    private Object Null;

    @RequestMapping("/")
    public List getAllEmployee()
    {
        return employeeService.getAllEmployees();
    }

    @RequestMapping("/{id}")
    public ResponseEntity findAllByEmpId(Model model, @PathVariable int id)
    {
        ResponseEntity <Employee> employeeResponse;

        if(employeeService.employeeExists(id))
            return new ResponseEntity<>(employeeService.findAllByEmpId(id), HttpStatus.OK);
        else
            return new ResponseEntity<>("Employee Does Not Exit", HttpStatus.BAD_REQUEST);
    }
}