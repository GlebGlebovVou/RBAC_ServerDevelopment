package com.gleb.taxi.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, length = 200)
    public String name;

    @Column(nullable = false, length = 320)
    public String email;

    @Column(length = 50)
    public String phone;

    @Column(name = "license_number", nullable = false, length = 100)
    public String licenseNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    public DriverStatus status;

    @Column(name = "created_at", nullable = false)
    public Instant createdAt;
}