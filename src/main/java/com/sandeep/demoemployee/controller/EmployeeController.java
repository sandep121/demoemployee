package com.sandeep.demoemployee.controller;

//import com.sandeep.demoemployee.entity.Employee;
import com.sandeep.demoemployee.entity.CrudeEmployee;
import com.sandeep.demoemployee.entity.Employee;
import com.sandeep.demoemployee.repository.DesignationRepository;
import com.sandeep.demoemployee.service.EmployeeService;
import com.sandeep.demoemployee.service.EmployeeValidationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")

@Api(value="Employee Management System")
public class EmployeeController
{
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DesignationRepository designationRepository;
    @Autowired
    private EmployeeValidationService validationService;


    @ApiOperation(value = "View a list of available employees", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping("/")
    public List<Employee> getAllEmployee()
    {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity findAllByEmpId(Model model, @PathVariable int id)
    {
        Map<String,List<Employee>> employeeResponse=new HashMap<>();
        employeeResponse.put("Employee", employeeService.findAllByEmpId(id));
        employeeResponse.put("Colleague", employeeService.getColleague(id));
        employeeResponse.put("Manager", employeeService.getManager(id));
        if(employeeService.employeeExists(id))
            return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
        else
            return new ResponseEntity<>("Employee Does Not Exit", HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>("Employee already exists.\nYou can keep empId field empty", HttpStatus.CONFLICT);
        }
        else if(!isValidParent)
        {
            return new ResponseEntity<>("Organisation hierarchy violated", HttpStatus.CONFLICT);
        }
        else
            employeeService.addEmployee(employee);

        return new ResponseEntity<>("Employee added successfully",HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<String> updateEmployee(@RequestBody CrudeEmployee crudeEmployee)
    {
        Employee empOld=employeeService.getEmpFromCrudeEmp(crudeEmployee);
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
            empNew.setEmpId(0);
            employeeService.updateManager(empOld.getEmpId(),employeeService.addEmployee(empNew));
            employeeService.deleteEmployee(empOld.getEmpId());
            return new ResponseEntity<>("Employee replaced successfully",HttpStatus.OK);
        }
        else
        {
            employeeService.addEmployee(empOld);
            return new ResponseEntity<>("Employee updated successfully",HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int id)
    {
        if(!employeeService.employeeExists(id))
        {
            return new ResponseEntity<>("The employee does not exist", HttpStatus.NOT_FOUND);
        }
        else if(employeeService.getEmployeeById(id).getDesignation().getDsgnId()==1)
            return new ResponseEntity<>("You cannot fire the director!!!",HttpStatus.FORBIDDEN);
        else if(employeeService.deleteEmployee(id))
        {
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Unknown error occured", HttpStatus.OK);
    }
}