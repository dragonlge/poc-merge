package com.poc.merge_poc.repository;

import com.poc.merge_poc.entity.models.EmployeeEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeEntityRepository extends JpaRepository<EmployeeEntity, String> {

    @Query(value = "FROM #{#entityName} ee WHERE ee.clientId=:clientId")
    @EntityGraph(attributePaths = {"employeeBinding"})
    List<EmployeeEntity> fetchAllByClientId(String clientId);
}
