package com.devbyjose.portfolio.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAiConfiguration {

    @Getter
    @Value("${apiKey}")
    private String apiKey;

    @Getter
    @Value("${apiUrl}")
    private String apiUrl;

    @Getter
    private String model_name = "gpt-3.5-turbo-0613";

    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}