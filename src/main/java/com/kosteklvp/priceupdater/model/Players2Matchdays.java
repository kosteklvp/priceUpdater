package com.kosteklvp.priceupdater.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(Players2MatchdaysID.class)
public class Players2Matchdays {

  @Id
  @ManyToOne
  @JoinColumn(name = "playerID", referencedColumnName = "ID")
  private Player player;

  @Id
  @ManyToOne
  @JoinColumn(name = "matchdayID", referencedColumnName = "ID")
  private Matchday matchday;

  private BigDecimal valueThen;

}
