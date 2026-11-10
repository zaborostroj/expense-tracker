package com.zaborostroj.expense.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "users", schema = "auth")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "uuid", nullable = false, updatable = false)
    private UUID uuid;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 512)
    private String passwordHash;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}
