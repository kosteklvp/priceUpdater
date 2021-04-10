package com.kosteklvp.priceupdater.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosteklvp.priceupdater.model.Matchday;

public interface MatchdayRepo extends JpaRepository<Matchday, Long> {

  Matchday findTopByOrderByIdDesc();

}
