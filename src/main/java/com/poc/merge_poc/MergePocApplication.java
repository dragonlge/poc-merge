package com.poc.merge_poc;

import com.poc.merge_poc.configuration.MergeConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(MergeConfigurationProperties.class)
@EnableFeignClients(basePackages = "com.poc.merge_poc.feigns")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class MergePocApplication {

	public static void main(String[] args) {
		SpringApplication.run(MergePocApplication.class, args);
	}

}
