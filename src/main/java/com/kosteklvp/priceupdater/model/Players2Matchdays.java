package com.kosteklvp.priceupdater.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class Players2Matchdays {

  @EmbeddedId
  private Players2MatchdaysID id;

  @ManyToOne
  @MapsId("playerId")
  @JoinColumn(name = "student_id")
  private Player player;

  @ManyToOne
  @MapsId("matchdayId")
  @JoinColumn(name = "matchday_id")
  private Matchday matchday;

  private double valueThen;

}
