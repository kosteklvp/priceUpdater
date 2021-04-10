package com.kosteklvp.priceupdater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kosteklvp.priceupdater.model.Player;

@Repository
public interface PlayerRepo extends JpaRepository<Player, Long> {

}
