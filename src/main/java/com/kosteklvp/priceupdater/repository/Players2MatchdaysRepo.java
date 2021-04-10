package com.kosteklvp.priceupdater.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosteklvp.priceupdater.model.Players2Matchdays;
import com.kosteklvp.priceupdater.model.Players2MatchdaysID;

public interface Players2MatchdaysRepo extends JpaRepository<Players2Matchdays, Players2MatchdaysID> {

}
