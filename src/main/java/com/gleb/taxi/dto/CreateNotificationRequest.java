package com.gleb.taxi.dto;

import com.gleb.taxi.model.RecipientType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateNotificationRequest(
        @NotNull Long tripId,
        @NotNull RecipientType recipientType,
        @NotNull Long recipientId,
        @NotBlank @Size(max = 2000) String message
) {
}