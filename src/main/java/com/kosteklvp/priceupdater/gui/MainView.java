package com.kosteklvp.priceupdater.gui;

import com.kosteklvp.priceupdater.model.Club;
import com.kosteklvp.priceupdater.repository.ClubRepo;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {

  private final ClubRepo clubRepo;
  final Grid<Club> grid;

  public MainView(ClubRepo clubRepo) {
    this.clubRepo = clubRepo;
    this.grid = new Grid<>(Club.class);
    add(grid);
    listClubs();
  }

  private void listClubs() {
    grid.setItems(clubRepo.findAll());
  }
}
