package com.kosteklvp.priceupdater.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kosteklvp.priceupdater.model.Matchday;
import com.kosteklvp.priceupdater.model.Players2Matchdays;
import com.kosteklvp.priceupdater.model.Players2MatchdaysID;

public interface Players2MatchdaysRepo extends JpaRepository<Players2Matchdays, Players2MatchdaysID> {

  List<Players2Matchdays> findByMatchday(Matchday matchday);

  @Query(nativeQuery = true)
  List<Object[]> getPriceChangesByMatchday(@Param("matchday") Matchday matchday);

}
