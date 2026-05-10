package com.gleb.taxi.controller;

import com.gleb.taxi.dto.CreateDriverRequest;
import com.gleb.taxi.dto.DriverResponse;
import com.gleb.taxi.dto.UpdateDriverStatusRequest;
import com.gleb.taxi.mapper.DriverMapper;
import com.gleb.taxi.model.Driver;
import com.gleb.taxi.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Tag(name = "Drivers", description = "Водители: CRUD, статус, список доступных (кэш Redis)")
@RestController
@RequiredArgsConstructor
public class DriverController {

    private final UserService userService;

    @PostMapping("/drivers")
    @ResponseStatus(HttpStatus.CREATED)
    public DriverResponse create(@Valid @RequestBody CreateDriverRequest req) {
        Driver d = userService.createDriver(req.name(), req.email(), req.phone(), req.licenseNumber());
        return DriverMapper.toResponse(d);
    }

    @GetMapping("/drivers/available")
    public List<DriverResponse> listAvailable() {
        return userService.listAvailableDrivers();
    }

    @GetMapping("/drivers/{id}")
    public DriverResponse get(@PathVariable long id) {
        Driver d = userService.getDriver(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found: " + id));
        return DriverMapper.toResponse(d);
    }

    @PatchMapping("/drivers/{id}/status")
    public DriverResponse updateStatus(@PathVariable long id, @Valid @RequestBody UpdateDriverStatusRequest req) {
        return DriverMapper.toResponse(userService.updateDriverStatus(id, req.status()));
    }
}