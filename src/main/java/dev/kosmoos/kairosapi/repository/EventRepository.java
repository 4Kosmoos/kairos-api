package dev.kosmoos.kairosapi.repository;

import dev.kosmoos.kairosapi.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
}