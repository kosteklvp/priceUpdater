package com.kosteklvp.priceupdater;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import com.kosteklvp.priceupdater.model.Club;
import com.kosteklvp.priceupdater.model.Matchday;
import com.kosteklvp.priceupdater.model.Player;
import com.kosteklvp.priceupdater.model.Players2Matchdays;
import com.kosteklvp.priceupdater.repository.MatchdayRepo;
import com.kosteklvp.priceupdater.repository.PlayerRepo;
import com.kosteklvp.priceupdater.repository.Players2MatchdaysRepo;
import com.kosteklvp.priceupdater.repository.TeamRepo;
import com.kosteklvp.priceupdater.utilities.TimeUtil;
import com.kosteklvp.priceupdater.utilities.UEFAGaming;

@Configuration
public class DatabasePreparation {

  private static final Logger log = LoggerFactory.getLogger(DatabasePreparation.class);

  @Autowired
  MatchdayRepo matchdayRepo;

  @Autowired
  PlayerRepo playerRepo;

//  @Bean
  public CommandLineRunner loadCurrentGameRound() {
    return args -> {
      URIBuilder uriBuilder = new URIBuilder(UEFAGaming.ServiceURL.LIVE_MIXED.get());
      String jsonString = getJSON(uriBuilder.build());

      JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
      JSONObject jsonData = (JSONObject) jsonObject.get(UEFAGaming.JSONKey.DATA.get());
      JSONObject jsonValue = (JSONObject) jsonData.get(UEFAGaming.JSONKey.VALUE.get());
      String jsonDeadline = (String) jsonValue.get(UEFAGaming.LiveMixedJSONKey.DEADLINE.get());
      JSONObject jsonFixture = (JSONObject) jsonValue.get(UEFAGaming.LiveMixedJSONKey.FIXTURE.get());

      matchdayRepo.save(Matchday.builder()
          .id((long) jsonFixture.get(UEFAGaming.LiveMixedJSONKey.MATCHDAY_ID.get()))
          .deadline(LocalDateTime.parse(jsonDeadline, TimeUtil.FORMAT_DATE_TIME))
          .build());

      for (int i = 1; i < 10; i++) {
        matchdayRepo.save(Matchday.builder().id(i).build());
      }
    };
  }

//  @Bean
  public CommandLineRunner loadClubs(TeamRepo teamRepo) {
    return args -> {
      Matchday currentMatchday = matchdayRepo.findTopByOrderByIdDesc();
      URIBuilder uriBuilder = new URIBuilder(UEFAGaming.ServiceURL.PLAYERS.get())
          .setParameter(UEFAGaming.PlayersJSONKey.GAMEDAY_ID.get(), Long.toString(currentMatchday.getId()))
          .setParameter(UEFAGaming.PlayersJSONKey.LANGUAGE.get(), UEFAGaming.DEFAULT_LANGUAGE);
      String jsonString = getJSON(uriBuilder.build());

      JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
      JSONObject jsonData = (JSONObject) jsonObject.get(UEFAGaming.JSONKey.DATA.get());
      JSONObject jsonValue = (JSONObject) jsonData.get(UEFAGaming.JSONKey.VALUE.get());
      JSONArray jsonPlayerList = (JSONArray) jsonValue.get(UEFAGaming.PlayersJSONKey.PLAYER_LIST.get());

      List<Club> teams = (List<Club>) jsonPlayerList.stream()
          .map(jsonPlayer -> Club.builder()
              .id(Long.parseLong((String) ((JSONObject) jsonPlayer).get(UEFAGaming.PlayersJSONKey.TEAM_ID.get())))
              .code((String) ((JSONObject) jsonPlayer).get(UEFAGaming.PlayersJSONKey.TEAM_CODE.get()))
              .name((String) ((JSONObject) jsonPlayer).get(UEFAGaming.PlayersJSONKey.TEAM_NAME.get()))
              .build())
          .distinct()
          .collect(Collectors.toList());

      teamRepo.saveAll(teams);
    };
  }

//  @Bean
  public CommandLineRunner loadPlayers(PlayerRepo playerRepo) {
    return args -> {
      Matchday currentMatchday = matchdayRepo.findTopByOrderByIdDesc();
      URIBuilder uriBuilder = new URIBuilder(UEFAGaming.ServiceURL.PLAYERS.get())
          .setParameter(UEFAGaming.PlayersJSONKey.GAMEDAY_ID.get(), Long.toString(currentMatchday.getId()))
          .setParameter(UEFAGaming.PlayersJSONKey.LANGUAGE.get(), UEFAGaming.DEFAULT_LANGUAGE);
      String jsonString = getJSON(uriBuilder.build());

      JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
      JSONObject jsonData = (JSONObject) jsonObject.get(UEFAGaming.JSONKey.DATA.get());
      JSONObject jsonValue = (JSONObject) jsonData.get(UEFAGaming.JSONKey.VALUE.get());
      JSONArray jsonPlayerList = (JSONArray) jsonValue.get(UEFAGaming.PlayersJSONKey.PLAYER_LIST.get());

      List<Player> players = (List<Player>) jsonPlayerList.stream()
          .map(jsonPlayer -> Player.builder()
              .id(Long.parseLong((String) ((JSONObject) jsonPlayer).get(UEFAGaming.PlayersJSONKey.ID.get())))
              .name((String) ((JSONObject) jsonPlayer).get(UEFAGaming.PlayersJSONKey.NAME.get()))
              .club(Club.builder()
                  .id(Long.parseLong((String) ((JSONObject) jsonPlayer).get(UEFAGaming.PlayersJSONKey.TEAM_ID.get())))
                  .code((String) ((JSONObject) jsonPlayer).get(UEFAGaming.PlayersJSONKey.TEAM_CODE.get()))
                  .name((String) ((JSONObject) jsonPlayer).get(UEFAGaming.PlayersJSONKey.TEAM_NAME.get()))
                  .build())
              .build())
          .collect(Collectors.toList());

      playerRepo.saveAll(players);
    };
  }

//  @Bean
  public CommandLineRunner test(Players2MatchdaysRepo players2MatchdaysRepo) {
    return args -> {
      Optional<Matchday> matchday = matchdayRepo.findById(Long.valueOf(1));
      Optional<Player> player = playerRepo.findById(Long.valueOf(48716));

      Players2Matchdays players2Matchdays = Players2Matchdays.builder().player(player.get()).matchday(matchday.get())
          .valueThen(34.5).build();

      players2MatchdaysRepo.save(players2Matchdays);
    };
  }

  @Bean
  public CommandLineRunner loadPrices(Players2MatchdaysRepo players2MatchdaysRepo) {
    return args -> {
      List<Players2Matchdays> players2Matchdays = new ArrayList<>();
      Matchday matchday = matchdayRepo.findById(Long.valueOf(10)).get();

      URIBuilder uriBuilder = new URIBuilder(UEFAGaming.ServiceURL.PLAYERS.get())
          .setParameter(UEFAGaming.PlayersJSONKey.GAMEDAY_ID.get(), Long.toString(matchday.getId()))
          .setParameter(UEFAGaming.PlayersJSONKey.LANGUAGE.get(), UEFAGaming.DEFAULT_LANGUAGE);
      String jsonString = getJSON(uriBuilder.build());

      JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
      JSONObject jsonData = (JSONObject) jsonObject.get(UEFAGaming.JSONKey.DATA.get());
      JSONObject jsonValue = (JSONObject) jsonData.get(UEFAGaming.JSONKey.VALUE.get());
      JSONArray jsonPlayerList = (JSONArray) jsonValue.get(UEFAGaming.PlayersJSONKey.PLAYER_LIST.get());

      List<Player> players = (List<Player>) jsonPlayerList.stream()
          .map(jsonPlayer -> Player.builder()
              .id(Long.parseLong((String) ((JSONObject) jsonPlayer).get(UEFAGaming.PlayersJSONKey.ID.get())))
              .name((String) ((JSONObject) jsonPlayer).get(UEFAGaming.PlayersJSONKey.NAME.get()))
              .value((double) ((JSONObject) jsonPlayer).get(UEFAGaming.PlayersJSONKey.VALUE.get()))
              .club(Club.builder()
                  .id(Long.parseLong((String) ((JSONObject) jsonPlayer).get(UEFAGaming.PlayersJSONKey.TEAM_ID.get())))
                  .build())
              .build())
          .collect(Collectors.toList());

      for (Player player : players) {
        players2Matchdays.add(Players2Matchdays.builder()
            .matchday(matchday)
            .player(player)
            .valueThen(player.getValue())
            .build());
      }

      players2MatchdaysRepo.saveAll(players2Matchdays);
    };
  }

  private String getJSON(URI uri) throws IOException {

    CloseableHttpClient httpClient = HttpClients.createDefault();

    try {

      HttpGet httpGet = new HttpGet(uri);

      CloseableHttpResponse response = httpClient.execute(httpGet);

      try {

        int statusCode = response.getStatusLine().getStatusCode();
        log.info("STATUS CODE: {}", statusCode);

        if (HttpStatus.valueOf(statusCode).is2xxSuccessful()) {
          HttpEntity entity = response.getEntity();

          if (entity != null) {
            return EntityUtils.toString(entity);
          }
        }

      } finally {
        response.close();
      }
    } finally {
      httpClient.close();
    }

    throw new IOException("Can't download json: " + uri.getPath());
  }

}
