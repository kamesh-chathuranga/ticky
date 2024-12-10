package com.ticky.backend.repository;

import com.ticky.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByVendorId(Long vendorId);
}
