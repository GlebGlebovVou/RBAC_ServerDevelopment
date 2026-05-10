package com.gleb.taxi.dto;

import com.gleb.taxi.model.NotificationTaskStatus;
import com.gleb.taxi.model.RecipientType;

public record NotificationResponse(
        long id,
        long tripId,
        RecipientType recipientType,
        long recipientId,
        String message,
        NotificationTaskStatus status,
        int attempts
) {
}