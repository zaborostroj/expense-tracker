package com.zaborostroj.tracker.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaborostroj.tracker.auth.domain.ErrorResponse;
import com.zaborostroj.tracker.auth.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class AppConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public AppConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain introspectFilterChain(HttpSecurity httpSecurity, UserDetailsService internalUserDetailsService) throws Exception {
        return httpSecurity
                .securityMatcher("/internal/auth/introspect")
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .httpBasic(basic -> {})
                .userDetailsService(internalUserDetailsService)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((req, resp, authEx) -> {
                            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                            resp.setContentType("application/json");
                            new ObjectMapper().writeValue(resp.getWriter(), new ErrorResponse("Unauthorized", authEx.getMessage()));
                        })
                        .accessDeniedHandler((req, resp, accessEx) -> {
                            resp.setStatus(HttpStatus.FORBIDDEN.value());
                            resp.setContentType("application/json");
                            new ObjectMapper().writeValue(resp.getWriter(), new ErrorResponse("AccessDenied", "You do not have permission to access this resource"));
                        })
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Qualifier("internalUserDetailsService")
    public UserDetailsService internalUserDetailsService(
            @Value("${auth.internal.username}") String username,
            @Value("${auth.internal.password}") String password,
            PasswordEncoder passwordEncoder
    ) {
        var user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles("INTERNAL_SERVICE")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
