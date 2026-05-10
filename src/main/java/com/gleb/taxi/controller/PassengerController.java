package com.gleb.taxi.controller;

import com.gleb.taxi.dto.CreatePassengerRequest;
import com.gleb.taxi.dto.PassengerResponse;
import com.gleb.taxi.mapper.PassengerMapper;
import com.gleb.taxi.model.Passenger;
import com.gleb.taxi.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "Passengers", description = "Пассажиры")
@RestController
@RequiredArgsConstructor
public class PassengerController {

    private final UserService userService;

    @PostMapping("/passengers")
    @ResponseStatus(HttpStatus.CREATED)
    public PassengerResponse create(@Valid @RequestBody CreatePassengerRequest req) {
        Passenger p = userService.createPassenger(req.name(), req.email(), req.phone());
        return PassengerMapper.toResponse(p);
    }

    @GetMapping("/passengers/{id}")
    public PassengerResponse get(@PathVariable long id) {
        Passenger p = userService.getPassenger(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Passenger not found: " + id));
        return PassengerMapper.toResponse(p);
    }
}