package com.kosteklvp.priceupdater.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

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
  @Column(name = "ID")
  private long id;

  private String name;

  @Transient
  private double value;

  @ManyToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "clubID")
  private Club club;

  @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
  private List<Players2Matchdays> players2matchdays;

}
