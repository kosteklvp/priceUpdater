package com.kosteklvp.priceupdater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kosteklvp.priceupdater.model.Club;

@Repository
public interface ClubRepo extends JpaRepository<Club, Long> {

}
