package com.kosteklvp.priceupdater.model;

import java.util.List;

import javax.persistence.CascadeType;
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
public class Club {

  @Id
  private long id;

  private String name;

  private String code;

  @OneToMany(mappedBy = "club", cascade = CascadeType.REMOVE)
  private List<Player> players;

}
