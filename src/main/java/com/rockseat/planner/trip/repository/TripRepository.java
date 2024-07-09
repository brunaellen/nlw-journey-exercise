package com.rockseat.planner.trip.repository;

import com.rockseat.planner.trip.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> {
}
