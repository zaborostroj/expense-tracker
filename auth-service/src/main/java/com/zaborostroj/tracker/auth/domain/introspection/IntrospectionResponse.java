package com.zaborostroj.tracker.auth.domain.introspection;

import java.util.UUID;

public record IntrospectionResponse(boolean active, UUID userUuid) {}
