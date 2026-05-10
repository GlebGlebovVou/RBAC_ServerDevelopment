package com.gleb.taxi.mapper;

import com.gleb.taxi.dto.NotificationResponse;
import com.gleb.taxi.model.NotificationTask;

public final class NotificationMapper {
    private NotificationMapper() {
    }

    public static NotificationResponse toResponse(NotificationTask t) {
        return new NotificationResponse(t.id, t.tripId, t.recipientType, t.recipientId, t.message, t.status, t.attempts);
    }
}