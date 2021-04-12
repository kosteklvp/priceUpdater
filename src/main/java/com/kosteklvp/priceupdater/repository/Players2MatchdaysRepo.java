package com.kosteklvp.priceupdater.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kosteklvp.priceupdater.model.Matchday;
import com.kosteklvp.priceupdater.model.Players2Matchdays;
import com.kosteklvp.priceupdater.model.Players2MatchdaysID;

public interface Players2MatchdaysRepo extends JpaRepository<Players2Matchdays, Players2MatchdaysID> {

  List<Players2Matchdays> findByMatchday(Matchday matchday);

  @Query(value = "SELECT t.playerName, t.clubName, t.value9, t.value10, (t.value10 - t.value9) AS priceChange FROM (SELECT player.name as playerName, club.name as clubName, (SELECT players2matchdays.valueThen FROM players2matchdays WHERE players2matchdays.matchdayID = 10 AND player.ID = players2matchdays.playerID) AS value10, (SELECT players2matchdays.valueThen FROM players2matchdays WHERE players2matchdays.matchdayID = 10 - 1 AND player.ID = players2matchdays.playerID) AS value9 FROM player inner join club on player.clubID = club.ID) AS t WHERE t.value10 - t.value9 <> 0 ORDER BY ABS(priceChange) DESC , clubName", nativeQuery = true)
  List<Object[]> getPriceChangesByGameday();

}
