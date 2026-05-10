package com.gleb.taxi.dto;

import com.gleb.taxi.model.DriverStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateDriverStatusRequest(@NotNull DriverStatus status) {
}