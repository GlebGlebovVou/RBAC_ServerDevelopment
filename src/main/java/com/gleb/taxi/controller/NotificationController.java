package com.gleb.taxi.controller;

import com.gleb.taxi.dto.CreateNotificationRequest;
import com.gleb.taxi.dto.NotificationResponse;
import com.gleb.taxi.mapper.NotificationMapper;
import com.gleb.taxi.model.NotificationTask;
import com.gleb.taxi.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Notifications", description = "Очередь уведомлений по поездкам")
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/notifications")
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationResponse create(@Valid @RequestBody CreateNotificationRequest req) {
        NotificationTask t = notificationService.enqueue(req.tripId(), req.recipientType(), req.recipientId(), req.message());
        return NotificationMapper.toResponse(t);
    }

    @GetMapping("/notifications")
    public List<NotificationResponse> getByTrip(@RequestParam("trip_id") long tripId) {
        return notificationService.getByTrip(tripId).stream().map(NotificationMapper::toResponse).toList();
    }
}