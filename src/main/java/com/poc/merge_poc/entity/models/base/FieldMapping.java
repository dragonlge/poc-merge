package com.poc.merge_poc.entity.models.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class FieldMapping {
    @JsonProperty("organization_defined_targets")
    private Map<String,String> organizationDefinedTargets;
    @JsonProperty("linked_account_defined_targets")
    private Map<String,String> linkedAccountDefinedTargets;
}
