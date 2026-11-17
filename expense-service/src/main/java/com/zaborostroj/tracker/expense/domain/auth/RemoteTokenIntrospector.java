package com.zaborostroj.tracker.expense.domain.auth;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.NonNull;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Log4j2
public class RemoteTokenIntrospector implements OpaqueTokenIntrospector {

    private final AuthClient authClient;
    private final Cache<String, OAuth2AuthenticatedPrincipal> tokenCache;

    @Override
    public @NonNull OAuth2AuthenticatedPrincipal introspect(@NonNull String token) {
        OAuth2AuthenticatedPrincipal cached = tokenCache.getIfPresent(token);
        if (cached != null) {
            log.debug("Token cache HIT for userId = {}", cached.getName());
            return cached;
        }
        log.debug("Token cache MISS. Call to auth-service");

        IntrospectionResponse response;
        try {
            response = authClient.introspect(token);
        } catch (Exception e) {
            throw new OAuth2IntrospectionException("Token introspection failed");
        }
        if (response == null || !response.isActive()) {
            throw new OAuth2IntrospectionException("Token is not active");
        }

        OAuth2AuthenticatedPrincipal principal = extractPrincipal(response);
        tokenCache.put(token, principal);
        return principal;
    }

    private static @NonNull OAuth2AuthenticatedPrincipal extractPrincipal(IntrospectionResponse response) {
        Map<String, Object> attributes = Map.of(
                "sub", String.valueOf(response.getUserUuid()),
                "userUuid", response.getUserUuid()
        );
        return new DefaultOAuth2AuthenticatedPrincipal(
                String.valueOf(response.getUserUuid()),
                attributes,
                Collections.emptyList()
        );
    }
}
