package com.rockseat.planner.trip.controller;

import com.rockseat.planner.openapi.api.TripsApi;
import com.rockseat.planner.openapi.model.CreatedTripResponse;
import com.rockseat.planner.openapi.model.TripCreateResponse;
import com.rockseat.planner.openapi.model.TripRequestPayload;
import com.rockseat.planner.participant.ParticipantService;
import com.rockseat.planner.trip.model.Trip;
import com.rockseat.planner.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TripController implements TripsApi {

  private final ParticipantService participantService;

  private final TripService tripService;

  @Override
  public ResponseEntity<TripCreateResponse> tripsPost(final TripRequestPayload tripRequestPayload) {
    final Trip trip = tripService.createTrip(tripRequestPayload);

    return ResponseEntity.ok(TripCreateResponse
        .builder()
        .id(trip.getId())
        .build());
  }

  @Override
  public ResponseEntity<CreatedTripResponse> tripsIdGet(final UUID id) {
    return tripService.getTripDetails(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
