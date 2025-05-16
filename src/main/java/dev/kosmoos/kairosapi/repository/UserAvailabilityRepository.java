package dev.kosmoos.kairosapi.repository;

import dev.kosmoos.kairosapi.entity.UserAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAvailabilityRepository
        extends JpaRepository<UserAvailability, Integer> {
}
