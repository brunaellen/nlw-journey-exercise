package com.rockseat.planner.trip.service;

import com.rockseat.planner.openapi.model.TripRequestPayload;
import com.rockseat.planner.trip.model.Trip;
import com.rockseat.planner.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static java.time.LocalDateTime.parse;

@RequiredArgsConstructor
@Service
public class TripService {
  private final TripRepository tripRepository;

    public Trip createTrip(final TripRequestPayload tripRequestPayload){
      final Trip trip = new Trip();
      trip.setDestination(tripRequestPayload.getDestination());
      trip.setStartsAt(setStartDateAndTimeFromString(tripRequestPayload.getStartsAt()));
      trip.setOwnerName(tripRequestPayload.getOwnerName());
      trip.setIsConfirmed(false);
      trip.setOwnerEmail(tripRequestPayload.getOwnerEmail());
      return tripRepository.save(trip);
  }

  private LocalDateTime setStartDateAndTimeFromString(final String startDateAndTime){
    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
        .ofPattern( "MM/dd/yyyy h:mm a", Locale.US);
    return parse(startDateAndTime, dateTimeFormatter);
  }
}
