package com.zaborostroj.tracker.expense.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
@EnableWebSecurity
public class AppConfig {

    @Bean
    public RestClient authRestClient(
            @Value("${auth.internal.url}") String baseUrl,
            @Value("${auth.internal.username}") String userName,
            @Value("${auth.internal.password}") String password
    ) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(3));
        factory.setReadTimeout(Duration.ofSeconds(5));

        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(headers -> headers.setBasicAuth(userName, password))
                .requestFactory(factory)
                .build();
    }

    @Bean
    public Cache<String, OAuth2AuthenticatedPrincipal> tokenCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(Duration.ofMinutes(5))
                .build();
    }
}
