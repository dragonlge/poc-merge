package com.poc.merge_poc.mapper;

import com.poc.merge_poc.entity.models.EmployeeEntity;
import com.merge.api.hris.types.Employee;
import com.merge.api.hris.types.EmployeeHomeLocation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeMapper {

    @ToEntity
    EmployeeEntity toEntity(Employee employee, String rawData,String clientId);


    default String mapLocation(Optional<EmployeeHomeLocation> value){
        return value.map(EmployeeHomeLocation::toString).orElse(null);
    }
    default <T> T map(Optional<T> value) {
        return value.orElse(null); // Return null or a default value if needed
    }
}
