package com.gleb.taxi.mapper;

import com.gleb.taxi.dto.DriverResponse;
import com.gleb.taxi.model.Driver;

public final class DriverMapper {
    private DriverMapper() {
    }

    public static DriverResponse toResponse(Driver d) {
        return new DriverResponse(d.id, d.name, d.email, d.phone, d.licenseNumber, d.status);
    }
}