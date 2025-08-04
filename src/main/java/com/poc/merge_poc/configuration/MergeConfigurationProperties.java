package com.poc.merge_poc.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "merge")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MergeConfigurationProperties {
    private String apiKey;

//    private String accountToken;
}
