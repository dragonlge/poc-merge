package com.poc.merge_poc.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BindingRequest (
        @JsonProperty("integration_employee_id") String integrationEmployeeId,
        @JsonProperty("horizon_employee_id") String horizonEmployeeId,
        @JsonProperty("horizon_user_id") Long horizonUserId
) {}