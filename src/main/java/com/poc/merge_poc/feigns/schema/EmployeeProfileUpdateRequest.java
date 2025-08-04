package com.poc.merge_poc.feigns.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmployeeProfileUpdateRequest {
    @JsonProperty("employee_id")
    private String employeeId;
    private String category;
    private String source;
    @JsonProperty("section_id")
    private Long sectionId;
    @JsonProperty("group_name")
    private String groupName;
    private Field[] fields;


    @Data
    public static class Field {
        private String field;
        private String field_type;
        private String value;
    }

    public static EmployeeProfileUpdateRequest newEmployeeProfileUpdateRequest(String employee_id, String firstName, String lastName, Long sectionId) {
        EmployeeProfileUpdateRequest request = new EmployeeProfileUpdateRequest();
        request.setEmployeeId(employee_id);
        request.setCategory("Personal");
        request.setSource("EmployeeForm");
        request.setSectionId(sectionId);
        request.setGroupName("Name");
        Field field1 = new Field();
        field1.setField("firstName");
        field1.setField_type("input");
        field1.setValue(firstName);
        Field field2 = new Field();
        field2.setField("lastName");
        field2.setField_type("input");
        field2.setValue(lastName);
        request.setFields(new Field[]{field1, field2});
        return request;
    }
}