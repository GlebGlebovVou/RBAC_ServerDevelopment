package com.gleb.taxi.mapper;

import com.gleb.taxi.dto.PassengerResponse;
import com.gleb.taxi.model.Passenger;

public final class PassengerMapper {
    private PassengerMapper() {
    }

    public static PassengerResponse toResponse(Passenger p) {
        return new PassengerResponse(p.id, p.name, p.email, p.phone);
    }
}