package com.kosteklvp.priceupdater.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class Players2MatchdaysID implements Serializable {

  @Column(name = "student_id")
  private long playerId;

  @Column(name = "course_id")
  private long matchdayId;

}
