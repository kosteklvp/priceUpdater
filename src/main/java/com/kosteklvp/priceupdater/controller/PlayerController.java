package com.kosteklvp.priceupdater.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kosteklvp.priceupdater.model.Player;
import com.kosteklvp.priceupdater.repository.PlayerRepo;

@RestController
public class PlayerController {

  @Autowired
  private PlayerRepo playerRepo;

  @GetMapping("/players")
  public List<Player> getPlayers() {
    return playerRepo.findAll();
  }

}
