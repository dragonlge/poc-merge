package com.poc.merge_poc.service;

import com.poc.merge_poc.configuration.MergeConfigurationProperties;
import com.merge.api.MergeApiClient;
import com.merge.api.MergeApiClientBuilder;
import com.merge.api.core.Environment;
import io.micrometer.common.util.StringUtils;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MergeClientFactory {

    private final MergeConfigurationProperties mergeConfigurationProperties;
    private final OkHttpClient httpClient;

    public MergeClientFactory(MergeConfigurationProperties mergeConfigurationProperties) {
        this.mergeConfigurationProperties = mergeConfigurationProperties;

        // Create reusable HTTP client
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.protocols(List.of(Protocol.HTTP_1_1));
        this.httpClient = clientBuilder.build();
    }

    public MergeApiClient createClient(String accountToken) {
        MergeApiClientBuilder mergeApiClientBuilder = MergeApiClient.builder()
                .httpClient(httpClient)
                .environment(Environment.PRODUCTION_EU)
                .timeout(60)
                .apiKey(mergeConfigurationProperties.getApiKey());
        if (StringUtils.isNotBlank(accountToken)) {
            mergeApiClientBuilder.accountToken(accountToken);
        }
        return mergeApiClientBuilder.build();
    }
}