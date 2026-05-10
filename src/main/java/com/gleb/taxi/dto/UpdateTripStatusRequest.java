package com.gleb.taxi.dto;

import com.gleb.taxi.model.TripStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateTripStatusRequest(@NotNull TripStatus status) {
}