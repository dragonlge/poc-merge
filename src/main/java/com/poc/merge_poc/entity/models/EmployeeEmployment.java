package com.poc.merge_poc.entity.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "employments")
public class EmployeeEmployment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employeeEntity;

    @JsonProperty("job_title")
    private String jobTitle;

    @JsonProperty("effective_date")
    private OffsetDateTime effectiveDate;

    @JsonProperty("employment_type")
    private String employmentType;

    // Additional fields as necessary
}