package com.poc.merge_poc.entity.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.poc.merge_poc.entity.models.base.MergeBaseEntity;
import com.merge.api.hris.types.*;
import com.merge.api.hris.types.EmployeeWorkLocation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.repository.EntityGraph;

import java.time.OffsetDateTime;
import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "employees")
@NamedEntityGraph(name = "EmployeeEntity.withBinding",
        attributeNodes = {@NamedAttributeNode("employeeBinding")})
public class EmployeeEntity extends MergeBaseEntity {

    @Column(name = "first_name")
    @JsonProperty("first_name")
    private String firstName;

    @Column(name = "employee_number")
    private String employeeNumber;

    private String lastName;

    private String preferredName;

    private String displayFullName;

    private String username;

    private String workEmail;

    private String personalEmail;

    private String mobilePhoneNumber;

    @Column(columnDefinition = "text")
    private String avatar;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "home_location", columnDefinition = "json")
    private  String homeLocation;

    @OneToOne(mappedBy = "employeeEntity",fetch = FetchType.LAZY)
    @JsonManagedReference
    private EmployeeBinding employeeBinding;

//    private  Optional<EmployeeWorkLocation> workLocation;
//
//    private final Optional<String> ssn;
//
//    private final Optional<EmployeeGender> gender;
//
//    private final Optional<EmployeeEthnicity> ethnicity;
//
//    private final Optional<EmployeeMaritalStatus> maritalStatus;
//
//    private final Optional<OffsetDateTime> dateOfBirth;
//
//    private final Optional<OffsetDateTime> hireDate;
//
//    private final Optional<OffsetDateTime> startDate;
//
//    private final Optional<OffsetDateTime> remoteCreatedAt;
//
//    private final Optional<EmployeeEmploymentStatus> employmentStatus;
//
//    private final Optional<OffsetDateTime> terminationDate;
//

}
