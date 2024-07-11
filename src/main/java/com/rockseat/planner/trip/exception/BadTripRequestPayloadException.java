package com.rockseat.planner.trip.exception;

import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The TripRequestPayload was missing required fields or was filled with wrong values")
@StandardException
public class BadTripRequestPayloadException extends RuntimeException {
}
