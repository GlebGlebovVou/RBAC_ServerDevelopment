package com.gleb.taxi.dto;

import com.gleb.taxi.model.DriverStatus;

public record DriverResponse(long id, String name, String email, String phone, String licenseNumber, DriverStatus status) {
}