package com.poc.merge_poc.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExchangeAccessToken(
        @JsonProperty("client_id") String clientId,
        @JsonProperty("client_name") String clientName,
        @JsonProperty("public_token") String publicToken) {
}
