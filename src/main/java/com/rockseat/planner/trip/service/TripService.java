package com.rockseat.planner.trip.service;

import com.rockseat.planner.openapi.model.CreatedTripResponse;
import com.rockseat.planner.openapi.model.TripRequestPayload;
import com.rockseat.planner.trip.exception.BadTripRequestPayloadException;
import com.rockseat.planner.trip.model.Trip;
import com.rockseat.planner.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static java.time.LocalDateTime.parse;

@RequiredArgsConstructor
@Service
public class TripService {
  private final TripRepository tripRepository;

    public Trip createTrip(final TripRequestPayload tripRequestPayload){
      if(ObjectUtils.isEmpty(tripRequestPayload)){
        throw new BadTripRequestPayloadException();
      }

      try {
        final Trip trip = new Trip();
        trip.setDestination(tripRequestPayload.getDestination());
        trip.setStartsAt(setDateAndTimeFromString(tripRequestPayload.getStartsAt()));
        trip.setEndsAt(setDateAndTimeFromString(tripRequestPayload.getEndsAt()));
        trip.setOwnerName(tripRequestPayload.getOwnerName());
        trip.setIsConfirmed(false);
        trip.setOwnerEmail(tripRequestPayload.getOwnerEmail());
        return tripRepository.save(trip);
      } catch (DateTimeParseException exception){
        throw new BadTripRequestPayloadException();
      }
    }

  public Optional<CreatedTripResponse> getTripDetails(final UUID id){
    final Optional<Trip> savedTrip = tripRepository.findById(id);

    return savedTrip
        .map(trip -> CreatedTripResponse
        .builder()
        .id(trip.getId())
        .destination(trip.getDestination())
        .startsAt(String.valueOf(trip.getStartsAt()))
        .endsAt(String.valueOf(trip.getEndsAt()))
        .isConfirmed(trip.getIsConfirmed())
        .ownerEmail(trip.getOwnerEmail())
        .ownerName(trip.getOwnerName())
        .build());
  }

  private LocalDateTime setDateAndTimeFromString(final String startDateAndTime){
    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
        .ofPattern( "MM/dd/yyyy h:mm a", Locale.US);
    return parse(startDateAndTime, dateTimeFormatter);
  }
}
