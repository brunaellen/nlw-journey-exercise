package com.rockseat.planner.trip.repository;

import com.rockseat.planner.trip.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TripRepository extends JpaRepository<Trip, UUID> {
}
