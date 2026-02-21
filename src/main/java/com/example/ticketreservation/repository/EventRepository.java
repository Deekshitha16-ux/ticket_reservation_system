package com.example.ticketreservation.repository;

import com.example.ticketreservation.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}