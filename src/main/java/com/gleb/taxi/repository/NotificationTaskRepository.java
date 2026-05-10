package com.gleb.taxi.repository;

import com.gleb.taxi.model.NotificationTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {
    List<NotificationTask> findByTripIdOrderByCreatedAtAsc(Long tripId);
}