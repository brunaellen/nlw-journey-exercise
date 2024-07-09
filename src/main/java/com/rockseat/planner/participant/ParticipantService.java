package com.rockseat.planner.participant;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

  public void registerParticipantsToEvent(final List<String> participantsToInvite, final UUID trip){
  }

  public void triggerConfirmationEmailToParticipants(final UUID tripId){
  }
}
