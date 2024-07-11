package com.rockseat.planner.trip.service;

import com.rockseat.planner.openapi.model.CreatedTripResponse;
import com.rockseat.planner.openapi.model.TripRequestPayload;
import com.rockseat.planner.trip.exception.BadTripRequestPayloadException;
import com.rockseat.planner.trip.model.Trip;
import com.rockseat.planner.trip.repository.TripRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
      .ofPattern( "MM/dd/yyyy h:mm a", Locale.US);

  @Mock
  private TripRepository tripRepository;

  @InjectMocks
  private TripService tripService;

  @Test
  void givenTripRequestPayload_createTrip_returnsSavedTrip() {
    final TripRequestPayload tripRequestPayload = TripRequestPayload
        .builder()
        .destination("Spain")
        .startsAt("01/08/2024 12:43 AM")
        .endsAt("11/08/2024 12:43 AM")
        .ownerName("Bruna")
        .ownerEmail("bruna@hotmail.com")
        .emailsToInvite(List.of("sara@hotmail.com", "amy@hotmail.com"))
        .build();

    final Trip expectedTrip = new Trip();
    expectedTrip.setDestination(tripRequestPayload.getDestination());
    expectedTrip.setStartsAt(LocalDateTime.parse(tripRequestPayload.getStartsAt(), DATE_TIME_FORMATTER));
    expectedTrip.setEndsAt(LocalDateTime.parse(tripRequestPayload.getEndsAt(), DATE_TIME_FORMATTER));
    expectedTrip.setOwnerName(tripRequestPayload.getOwnerName());
    expectedTrip.setIsConfirmed(false);
    expectedTrip.setOwnerEmail(tripRequestPayload.getOwnerEmail());

    when(tripRepository.save(expectedTrip)).thenReturn(expectedTrip);

    final Trip trip = tripService.createTrip(tripRequestPayload);
    assertThat(trip).isEqualTo(expectedTrip);
  }

  @Test
  void givenNoTripRequestPayloadDetails_createTrip_throwsBadTripRequestPayloadException() {
    final TripRequestPayload tripRequestPayload = TripRequestPayload
        .builder()
        .build();

    assertThatThrownBy(() -> tripService.createTrip(tripRequestPayload))
        .isInstanceOf(BadTripRequestPayloadException.class);
  }

  @Test
  void givenWrongTripRequestPayloadDetails_createTrip_throwsBadTripRequestPayloadException() {
    final TripRequestPayload tripRequestPayload = TripRequestPayload
        .builder()
        .startsAt("wrong data")
        .build();

    assertThatThrownBy(() -> tripService.createTrip(tripRequestPayload))
        .isInstanceOf(BadTripRequestPayloadException.class);
  }

  @Test
  void givenNotFoundId_getTripDetails() {
    when(tripRepository.findById(any())).thenReturn(Optional.empty());

    assertThat(tripService.getTripDetails(UUID.randomUUID())).isEmpty();
  }

  @Test
  void givenId_getTripDetails_ReturnsCreatedTripResponse() {
    final Trip trip = new Trip();
    trip.setDestination("Spain");
    trip.setStartsAt(LocalDateTime.parse("01/08/2024 12:43 AM", DATE_TIME_FORMATTER));
    trip.setEndsAt(LocalDateTime.parse("11/08/2024 12:43 AM", DATE_TIME_FORMATTER));
    trip.setOwnerName("Bruna");
    trip.setOwnerEmail("bruna@hotmail.com");
    trip.setIsConfirmed(false);

    final CreatedTripResponse expectedCreatedTripResponse = CreatedTripResponse
        .builder()
        .id(trip.getId())
        .destination(trip.getDestination())
        .startsAt(String.valueOf(trip.getStartsAt()))
        .endsAt(String.valueOf(trip.getEndsAt()))
        .isConfirmed(trip.getIsConfirmed())
        .ownerEmail(trip.getOwnerEmail())
        .ownerName(trip.getOwnerName())
        .build();

    when(tripRepository.findById(any())).thenReturn(Optional.of(trip));

    assertThat(tripService.getTripDetails(UUID.randomUUID())).isEqualTo(Optional.of(expectedCreatedTripResponse));
  }
}