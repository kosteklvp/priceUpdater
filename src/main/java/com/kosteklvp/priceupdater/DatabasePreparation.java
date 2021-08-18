package com.kosteklvp.priceupdater;

import static java.util.stream.Collectors.toSet;
import static org.springframework.util.ObjectUtils.isEmpty;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kosteklvp.priceupdater.model.Club;
import com.kosteklvp.priceupdater.model.Player;
import com.kosteklvp.priceupdater.repository.ClubRepo;
import com.kosteklvp.priceupdater.repository.PlayerRepo;
import com.kosteklvp.priceupdater.uefa.UEFAGaming;

@Configuration
public class DatabasePreparation {

  private static final Path MAIN_JSON_FILE = Path.of("./src/main/resources/JSONFiles/PlayersGW1.json");

  private static final Logger log = LoggerFactory.getLogger(DatabasePreparation.class);

  @Autowired
  PlayerRepo playerRepo;

  @Autowired
  ClubRepo clubRepo;

  @Bean
  public CommandLineRunner loadDataFromJSONFile() {
    return args -> {
      String jsonString = Files.readString(MAIN_JSON_FILE);

      if (isEmpty(jsonString)) {
        return;
      }

      loadClubs(jsonString);
      loadPlayers(jsonString);
    };
  }

  private void loadClubs(String jsonString) throws ParseException {
    Objects.requireNonNull(jsonString);

    JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
    JSONObject jsonData = (JSONObject) jsonObject.get(UEFAGaming.JSONKey.DATA.get());
    JSONObject jsonValue = (JSONObject) jsonData.get(UEFAGaming.JSONKey.VALUE.get());
    JSONArray jsonPlayerList = (JSONArray) jsonValue.get(UEFAGaming.PlayersJSONKey.PLAYER_LIST.get());

    Set<Club> clubs = (Set<Club>) jsonPlayerList.stream()
        .map(jsonPlayer -> Club.builder()
            .id(Long.parseLong((String) ((JSONObject) jsonPlayer).get(UEFAGaming.PlayersJSONKey.TEAM_ID.get())))
            .code((String) ((JSONObject) jsonPlayer).get(UEFAGaming.PlayersJSONKey.TEAM_CODE.get()))
            .name((String) ((JSONObject) jsonPlayer).get(UEFAGaming.PlayersJSONKey.TEAM_NAME.get()))
            .build())
        .collect(toSet());

    clubRepo.saveAll(clubs);
    log.info(MessageFormat.format("Loaded {0} clubs to database.", clubs.size()));
  }

  private void loadPlayers(String jsonString) throws ParseException {
    Objects.requireNonNull(jsonString);

    JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
    JSONObject jsonData = (JSONObject) jsonObject.get(UEFAGaming.JSONKey.DATA.get());
    JSONObject jsonValue = (JSONObject) jsonData.get(UEFAGaming.JSONKey.VALUE.get());
    JSONArray jsonPlayerList = (JSONArray) jsonValue.get(UEFAGaming.PlayersJSONKey.PLAYER_LIST.get());

    Set<Player> players = (Set<Player>) jsonPlayerList.stream()
        .map(jsonPlayer -> Player.builder()
            .id(Long.parseLong((String) ((JSONObject) jsonPlayer).get(UEFAGaming.PlayersJSONKey.ID.get())))
            .name((String) ((JSONObject) jsonPlayer).get(UEFAGaming.PlayersJSONKey.NAME.get()))
            .club(Club.builder()
                .id(Long.parseLong((String) ((JSONObject) jsonPlayer).get(UEFAGaming.PlayersJSONKey.TEAM_ID.get())))
                .code((String) ((JSONObject) jsonPlayer).get(UEFAGaming.PlayersJSONKey.TEAM_CODE.get()))
                .name((String) ((JSONObject) jsonPlayer).get(UEFAGaming.PlayersJSONKey.TEAM_NAME.get()))
                .build())
            .build())
        .collect(toSet());

    playerRepo.saveAll(players);
    log.info(MessageFormat.format("Loaded {0} players to database.", players.size()));
  }

}
