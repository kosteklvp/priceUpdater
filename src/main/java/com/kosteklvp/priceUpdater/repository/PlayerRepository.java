package com.kosteklvp.priceUpdater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kosteklvp.priceUpdater.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

}
