package com.kosteklvp.priceupdater.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Matchday {

  @Id
  private long id;

  private LocalDateTime deadline;

  @OneToMany(mappedBy = "matchday")
  private List<Players2Matchdays> players2matchdays;

}
