package com.gleb.taxi.service;

import com.gleb.taxi.config.RedisCacheConfig;
import com.gleb.taxi.dto.DriverResponse;
import com.gleb.taxi.mapper.DriverMapper;
import com.gleb.taxi.model.DriverStatus;
import com.gleb.taxi.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CachedAvailableDriversService {

    private final DriverRepository driverRepository;

    @Cacheable(cacheNames = RedisCacheConfig.AVAILABLE_DRIVERS_CACHE, key = "'all'")
    public List<DriverResponse> listAvailable() {
        return driverRepository.findByStatusOrderById(DriverStatus.AVAILABLE).stream()
                .map(DriverMapper::toResponse)
                .toList();
    }
}