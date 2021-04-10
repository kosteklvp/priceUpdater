package com.kosteklvp.priceupdater.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class Player {

  @Id
  private long id;

  private String name;

  @ManyToOne
  @JoinColumn(name = "club_id")
  private Club club;

  @OneToMany(mappedBy = "player")
  private List<Players2Matchdays> players2matchdays;

}
