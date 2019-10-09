package com.sandeep.demoemployee.repository;

import com.sandeep.demoemployee.entity.Employee;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Integer>
{
    List<Employee> findAllByOrderByDesignation_lvlIdAscEmpNameAsc();
    boolean existsAllByEmpIdIs(int id);
    List<Employee> findAllByManagerId(int id);
    Long countAllByDesignation_DsgnId(int id);
    Employee findByEmpId(Integer id);
}