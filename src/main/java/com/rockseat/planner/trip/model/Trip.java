package com.rockseat.planner.trip.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Trip {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private String destination;

  @Column(name = "starts_at")
  private LocalDateTime startsAt;

  @Column(name = "is_confirmed")
  private Boolean isConfirmed;

  @Column(name = "owner_name")
  private String ownerName;

  @Column(name = "owner_email")
  private String ownerEmail;
}
