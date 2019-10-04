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
    //@Query("SELECT a FROM Employee a WHERE a.title=:title and a.category=:category")
    //List<Employee> fetchEmployee(@Param("title") String title, @Param("category") String category);
    List<Employee> findAllByManagerId(int id);
    //List<Employee> findAllByManagerIdOrderByDesignation_lvlAscEmpNameAsc(int id);

}