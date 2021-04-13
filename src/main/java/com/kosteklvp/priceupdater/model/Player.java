package com.kosteklvp.priceupdater.model;

import java.math.BigDecimal;
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties("players2matchdays")
public class Player {

  @Id
  @Column(name = "ID")
  private long id;

  private String name;

  @Transient
  private BigDecimal value;

  @ManyToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "clubID")
  @JsonIgnoreProperties("players")
  private Club club;

  @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
  private List<Players2Matchdays> players2matchdays;

}
