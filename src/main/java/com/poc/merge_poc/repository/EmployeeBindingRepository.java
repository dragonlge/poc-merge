package com.poc.merge_poc.repository;

import com.poc.merge_poc.entity.models.EmployeeBinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeBindingRepository extends JpaRepository<EmployeeBinding, Long> {

    EmployeeBinding findByHorizonsEmployeeIdOrEmployeeEntity_Id(String horizonEmployeeId, String id);
}
