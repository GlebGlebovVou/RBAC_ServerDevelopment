package com.gleb.taxi.controller;

import com.gleb.taxi.dto.CreateTripRequest;
import com.gleb.taxi.dto.TripResponse;
import com.gleb.taxi.dto.UpdateTripRatingRequest;
import com.gleb.taxi.dto.UpdateTripStatusRequest;
import com.gleb.taxi.mapper.TripMapper;
import com.gleb.taxi.model.Trip;
import com.gleb.taxi.service.TripService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Tag(name = "Trips", description = "Поездки: создание, статус, рейтинг, список пассажира")
@RestController
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping("/trips")
    @ResponseStatus(HttpStatus.CREATED)
    public TripResponse create(@Valid @RequestBody CreateTripRequest req) {
        Trip t = tripService.createTrip(req.passengerId(), req.origin(), req.destination(), req.distanceKm());
        return TripMapper.toResponse(t);
    }

    @GetMapping("/trips/{id}")
    public TripResponse get(@PathVariable long id) {
        Trip t = tripService.getTrip(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found: " + id));
        return TripMapper.toResponse(t);
    }

    @GetMapping("/trips")
    public List<TripResponse> getByPassenger(@RequestParam("passenger_id") long passengerId) {
        return tripService.getPassengerTrips(passengerId).stream().map(TripMapper::toResponse).toList();
    }

    @PatchMapping("/trips/{id}/status")
    public TripResponse updateStatus(@PathVariable long id, @Valid @RequestBody UpdateTripStatusRequest req) {
        return TripMapper.toResponse(tripService.updateStatus(id, req.status()));
    }

    @PatchMapping("/trips/{id}/rating")
    public TripResponse rate(@PathVariable long id, @Valid @RequestBody UpdateTripRatingRequest req) {
        return TripMapper.toResponse(tripService.rateTrip(id, req.rating()));
    }
}