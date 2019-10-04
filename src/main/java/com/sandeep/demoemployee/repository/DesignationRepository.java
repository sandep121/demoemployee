package com.sandeep.demoemployee.repository;

import com.sandeep.demoemployee.entity.Designation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DesignationRepository extends CrudRepository<Designation, Integer>
{
}
