package com.poc.merge_poc.configuration;

import com.merge.api.MergeApiClient;
import com.merge.api.core.Environment;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.List;

@Configuration
public class MergeConfiguration {

    private final MergeConfigurationProperties mergeConfigurationProperties;

    public MergeConfiguration(MergeConfigurationProperties mergeConfigurationProperties) {
        this.mergeConfigurationProperties = mergeConfigurationProperties;
    }

    @Bean
    MergeApiClient createClient() {
        // Swap YOUR_API_KEY below with your production key from:
        // https://app.merge.dev/keys
        String apiKey = mergeConfigurationProperties.getApiKey();

        // The string 'TEST_ACCOUNT_TOKEN' below works to test your connection
        // to Merge and will return dummy data in the response.
        // In production, replace this with account_token from user.
//        String accountToken = mergeConfigurationProperties.getAccountToken();
        // Create an OkHttpClient builder
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        // Configure the client to only use HTTP/1.1 protocol
        clientBuilder.protocols(List.of(Protocol.HTTP_1_1));

        // Build the OkHttpClient instance
        OkHttpClient client = clientBuilder.build();


        return MergeApiClient.builder()
                .httpClient(client)
                .environment(Environment.PRODUCTION_EU)
//                .accountToken(accountToken)
                .timeout(60)
                .apiKey(apiKey)
                .build();
    }
}
