package com.poc.merge_poc.entity.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "work_locations")
public class EmployeeWorkLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("remote_id")
    private String remoteId;

    @JsonProperty("clientName")
    private String name;

}