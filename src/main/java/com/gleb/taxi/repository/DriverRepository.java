package com.gleb.taxi.repository;

import com.gleb.taxi.model.Driver;
import com.gleb.taxi.model.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    List<Driver> findByStatusOrderById(DriverStatus status);
}