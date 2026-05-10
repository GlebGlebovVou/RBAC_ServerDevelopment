package com.gleb.taxi.mapper;

import com.gleb.taxi.dto.TripResponse;
import com.gleb.taxi.model.Trip;

public final class TripMapper {
    private TripMapper() {
    }

    public static TripResponse toResponse(Trip t) {
        return new TripResponse(t.id, t.passengerId, t.driverId, t.status, t.origin, t.destination, t.distanceKm, t.price, t.rating);
    }
}