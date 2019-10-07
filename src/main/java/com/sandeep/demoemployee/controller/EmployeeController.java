package com.sandeep.demoemployee.controller;

import com.sandeep.demoemployee.entity.CrudeEmployee;
import com.sandeep.demoemployee.entity.Employee;
import com.sandeep.demoemployee.service.EmployeeService;
import com.sandeep.demoemployee.service.EmployeeValidationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
@Api
public class EmployeeController
{
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeValidationService validationService;


    @ApiOperation(value = "View a list of available employees", response = List.class)
    @GetMapping("/")
    public List<Employee> getAllEmployee()
    {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity findAllByEmpId(@PathVariable int id)
    {
        Map<String,List<Employee>> employeeResponse=new HashMap<>();
        employeeResponse.put("Employee", employeeService.findAllByEmpId(id));
        employeeResponse.put("Colleague", employeeService.getColleague(id));
        employeeResponse.put("Manager", employeeService.getManager(id));
        if(employeeService.employeeExists(id))
            return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
        else
            return new ResponseEntity<>( "Employee does not exist",HttpStatus.NOT_FOUND);
    }
    @GetMapping("/CountDsgId={id}")
    public ResponseEntity countByDesignationId(@PathVariable int id)
    {
        return new ResponseEntity<>(employeeService.getTotalEmployeeByDesignation(id),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addEmployee(@RequestBody CrudeEmployee crudeEmployee)
    {
        //crude is needed for designation field
        Employee employee=employeeService.getEmpFromCrudeEmp(crudeEmployee);
        boolean isValidEmpId= !validationService.employeeAlreadyExists(employee.getEmpId());
        boolean isValidParent=validationService.parentIsValid(employee);
        if(!isValidEmpId)
        {
            return new ResponseEntity<>("Employee already exists.\nYou can keep empId field empty", HttpStatus.BAD_REQUEST);
        }
        else if(!isValidParent)
        {
            return new ResponseEntity<>("Invalid superior", HttpStatus.BAD_REQUEST);
        }
        else if(employee.getDesignation().getDsgnId()==1 && employeeService.getTotalEmployeeByDesignation(1)!=0)
        {
            return new ResponseEntity<>("One Director is enough for the organisation", HttpStatus.FORBIDDEN);
        }
        else
            employeeService.addEmployee(employee);

        return new ResponseEntity<>("Employee added successfully",HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<String> updateEmployee(@RequestBody CrudeEmployee crudeEmployee)
    {
        System.out.println(crudeEmployee.isReplace());
        Employee empOld=employeeService.getEmpFromCrudeEmp(crudeEmployee);
        //check if the employee exists or not
        if(!employeeService.employeeExists(empOld.getEmpId()))
        {
            return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
        }
        else if(!validationService.parentIsValid(empOld))
        {
            return new ResponseEntity<>("Invalid superior",HttpStatus.CONFLICT);
        }
        else if(crudeEmployee.isReplace())
        {
            Employee empNew=new Employee(empOld);
            if(employeeService.getEmployeeById(empOld.getEmpId()).getDesignation().getDsgnId() != empNew.getDesignation().getDsgnId()
                    && (empNew.getDesignation().getDsgnId()==1
                    || employeeService.getEmployeeById(empOld.getEmpId()).getDesignation().getDsgnId() == 1))
                return new ResponseEntity<>("There should be one and only one Director in the organisation", HttpStatus.BAD_REQUEST);
            System.out.println("old dsgnId = "+ employeeService.getEmployeeById(empOld.getEmpId()).getDesignation().getDsgnId() + "\n new dsgnId = " + empNew.getDesignation().getDsgnId());
            empNew.setEmpId(0);
            employeeService.updateManager(empOld.getEmpId(),employeeService.addEmployee(empNew));
            employeeService.deleteEmployee(empOld.getEmpId());
            return new ResponseEntity<>("Employee replaced successfully",HttpStatus.OK);
        }
        else if(employeeService.updateEmployee(empOld, employeeService.getEmployeeById(empOld.getEmpId())))
        {
            return new ResponseEntity<>("Employee updated successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid Request",HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int id)
    {
        if(!employeeService.employeeExists(id))
        {
            return new ResponseEntity<>("The employee does not exist", HttpStatus.NOT_FOUND);
        }
        else if(employeeService.getEmployeeById(id).getDesignation().getDsgnId()==1 && employeeService.getTotalEmployee()!=1)
        {

            return new ResponseEntity<>("You cannot fire the director!!!",HttpStatus.FORBIDDEN);
        }
        else if(employeeService.deleteEmployee(id))
        {
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}