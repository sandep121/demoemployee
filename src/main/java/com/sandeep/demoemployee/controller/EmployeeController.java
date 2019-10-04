package com.sandeep.demoemployee.controller;

//import com.sandeep.demoemployee.entity.Employee;
import com.sandeep.demoemployee.entity.CrudeEmployee;
import com.sandeep.demoemployee.entity.Employee;
import com.sandeep.demoemployee.repository.DesignationRepository;
import com.sandeep.demoemployee.service.EmployeeService;
import com.sandeep.demoemployee.service.EmployeeValidationService;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PessimisticLockScope;
import javax.print.attribute.standard.Media;
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
    @Autowired
    private DesignationRepository designationRepository;
    @Autowired
    private EmployeeValidationService validationService;

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

    @PostMapping
    public ResponseEntity addEmployee(@RequestBody CrudeEmployee crudeEmployee)
    {
        Employee employee=employeeService.getEmpFromCrudeEmp(crudeEmployee);
        boolean isValidEmpId= !validationService.employeeAlreadyExists(employee.getEmpId());
        boolean isValidParent=validationService.parentIsValid(employee);
        if(!isValidEmpId)
        {
            return new ResponseEntity<>("Employee already exists", HttpStatus.CONFLICT);
        }
        else if(!isValidParent)
        {
            return new ResponseEntity("Organisation hierarchy violated", HttpStatus.CONFLICT);
        }
        else
            employeeService.addEmployee(employee);

        return new ResponseEntity<>("we are working on it",HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity udateEmployee(@RequestBody CrudeEmployee crudeEmployee)
    {
        Employee employee=employeeService.getEmpFromCrudeEmp(crudeEmployee);

        return new ResponseEntity("we are working on it", HttpStatus.OK);
    }
}