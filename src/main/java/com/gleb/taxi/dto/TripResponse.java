package com.gleb.taxi.dto;

import com.gleb.taxi.model.TripStatus;

import java.math.BigDecimal;

public record TripResponse(
        long id,
        long passengerId,
        Long driverId,
        TripStatus status,
        String origin,
        String destination,
        BigDecimal distanceKm,
        BigDecimal price,
        Integer rating
) {
}