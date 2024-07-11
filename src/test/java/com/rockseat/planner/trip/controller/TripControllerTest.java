package com.rockseat.planner.trip.controller;

import com.rockseat.planner.openapi.model.TripCreateResponse;
import com.rockseat.planner.openapi.model.TripRequestPayload;
import com.rockseat.planner.participant.ParticipantService;
import com.rockseat.planner.trip.exception.BadTripRequestPayloadException;
import com.rockseat.planner.trip.model.Trip;
import com.rockseat.planner.trip.service.TripService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static java.time.LocalDateTime.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripControllerTest {

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
      .ofPattern( "MM/dd/yyyy h:mm a", Locale.US);

  @Mock
  private ParticipantService participantService;

  @Mock
  private TripService tripService;

  @InjectMocks
  private TripController tripController;

  @Test
  void givenTripRequestPayload_tripsPost_returns200AndTripId() {
    final TripRequestPayload tripRequestPayload = TripRequestPayload
        .builder()
        .destination("Spain")
        .startsAt("01/08/2024 12:43 AM")
        .endsAt("11/08/2024 12:43 AM")
        .ownerName("Bruna")
        .ownerEmail("bruna@hotmail.com")
        .emailsToInvite(List.of("sara@hotmail.com", "amy@hotmail.com"))
        .build();

    final Trip trip = new Trip();
    trip.setId(UUID.randomUUID());
    trip.setDestination(tripRequestPayload.getDestination());
    trip.setStartsAt(LocalDateTime.parse(tripRequestPayload.getStartsAt(), DATE_TIME_FORMATTER));
    trip.setEndsAt(LocalDateTime.parse(tripRequestPayload.getEndsAt(), DATE_TIME_FORMATTER));
    trip.setOwnerName(tripRequestPayload.getOwnerName());
    trip.setIsConfirmed(false);
    trip.setOwnerEmail(tripRequestPayload.getOwnerEmail());

    when(tripService.createTrip(tripRequestPayload)).thenReturn(trip);

    final ResponseEntity<TripCreateResponse> tripCreateResponseResponseEntity = tripController
        .tripsPost(tripRequestPayload);

    assertThat(tripCreateResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(tripCreateResponseResponseEntity.getBody())
        .isEqualTo(TripCreateResponse.builder().id(trip.getId()).build());
  }

  @Test
  void givenNoTripRequestPayload_tripsPost_ThrowsBadTripRequestPayloadException() {
    final TripRequestPayload tripRequestPayload = TripRequestPayload
        .builder()
        .build();

    when(tripService.createTrip(tripRequestPayload)).thenThrow(new BadTripRequestPayloadException());

    assertThatThrownBy(() -> tripController.tripsPost(tripRequestPayload))
        .isInstanceOf(BadTripRequestPayloadException.class);
  }

  @Test
  void givenInvalidTripRequestPayload_tripsPost_ThrowsBadTripRequestPayloadException() {
    final TripRequestPayload tripRequestPayload = TripRequestPayload
        .builder()
        .destination("Spain")
        .startsAt("invalid")
        .endsAt("invalid")
        .ownerName("Bruna")
        .ownerEmail("bruna@hotmail.com")
        .emailsToInvite(List.of("sara@hotmail.com", "amy@hotmail.com"))
        .build();

    when(tripService.createTrip(tripRequestPayload)).thenThrow(new BadTripRequestPayloadException());

    assertThatThrownBy(() -> tripController.tripsPost(tripRequestPayload))
        .isInstanceOf(BadTripRequestPayloadException.class);
  }

  @Test
  void tripsIdGet() {
  }
}