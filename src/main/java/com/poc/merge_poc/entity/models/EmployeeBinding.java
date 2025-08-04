package com.poc.merge_poc.entity.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class EmployeeBinding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String horizonsEmployeeId;

    private Long horizonsUserId;

    @OneToOne
    @JoinColumn(name = "employee_id")
    @JsonBackReference
    private EmployeeEntity employeeEntity;
}
