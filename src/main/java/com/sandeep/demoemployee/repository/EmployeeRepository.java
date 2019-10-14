package com.sandeep.demoemployee.repository;

import com.sandeep.demoemployee.entity.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Integer>
{
    List<Employee> findAllByOrderByDesignation_lvlIdAscEmpNameAsc();
    boolean existsAllByEmpIdIs(int id);
    List<Employee> findAllByManagerIdOrderByDesignation_lvlIdAscEmpNameAsc(int id);
    //List<Employee> findAllByManagerIdOrderByDesignation_lvlAscEmpNameAsc(int id);
    Long countAllByDesignation_DsgnId(int id);
    Employee findByEmpId(Integer id);
}