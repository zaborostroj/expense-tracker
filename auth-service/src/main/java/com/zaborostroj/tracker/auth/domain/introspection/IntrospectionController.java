package com.zaborostroj.tracker.auth.domain.introspection;

import com.zaborostroj.tracker.auth.domain.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/auth")
@RequiredArgsConstructor
public class IntrospectionController {

    private final AuthService authService;

    @PostMapping("/introspect")
    public IntrospectionResponse introspect(@RequestBody IntrospectionRequest request) {
        return authService.introspectToken(request.token());
    }
}
