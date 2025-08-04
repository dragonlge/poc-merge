package com.poc.merge_poc.mapper;


import org.mapstruct.Mapping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@Mapping(target = "rawData", source = "rawData")
@Mapping(target = "clientId", source = "clientId")
public @interface ToEntity { }

