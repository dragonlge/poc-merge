package com.poc.merge_poc.feigns;

import com.poc.merge_poc.feigns.schema.EmployeeProfileUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "employeeClient", url = "${employee.client.url}/employee/api/v1")
public interface HorizonsEmployee {

    @GetMapping("/employees/v2")
    String getEmployees(
            @RequestParam("pagable") boolean pagable,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    );

    @GetMapping(value = "/employee_profile/infos/{employeeId}?category=Personal", consumes = MediaType.APPLICATION_JSON_VALUE)
    String getEmployeeProfileFields(
            @PathVariable("employeeId") String employeeId,
            @RequestHeader("Authorization") String authorization
    );

    @PutMapping(value = "/employee_profile/infos/{employeeId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateEmployeeProfile(
            @PathVariable("employeeId") String employeeId,
            @RequestHeader("Authorization") String authorization,
            @RequestBody EmployeeProfileUpdateRequest request
    );


}