package com.poc.merge_poc.entity.models.base;

import lombok.Data;

import java.util.List;

@Data
public class RemoteData {
    private String path;
    private List<String> data;
}
