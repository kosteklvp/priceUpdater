package com.kosteklvp.priceUpdater;

import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.kosteklvp.priceUpdater.repository.PlayerRepository;

@SpringBootApplication
public class PriceUpdaterApplication {

  private static final Logger log = LoggerFactory.getLogger(PriceUpdaterApplication.class);

  @Autowired
  private PlayerRepository playerRepository;

  public static void main(String[] args) {
    SpringApplication.run(PriceUpdaterApplication.class, args);
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.build();
  }

  @SuppressWarnings("unchecked")
  @Bean
  public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
    return args -> {
      String url = "https://gaming.uefa.com/en/uclfantasy/services/api/Feed/players?gamedayId=10&language=en";
      ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

      JSONObject jsonObject = (JSONObject) JSONValue.parse(response.getBody());
      JSONObject jsonData = (JSONObject) jsonObject.get(RequestKey.DATA.get());
      JSONObject jsonValue = (JSONObject) jsonData.get(RequestKey.VALUE.get());
      JSONArray jsonPlayerList = (JSONArray) jsonValue.get(RequestKey.PLAYER_LIST.get());

      List<Player> asd = (List<Player>) jsonPlayerList.stream()
          .map(jsonPlayer -> Player.builder()
              .id(Long.parseLong((String) ((JSONObject) jsonPlayer).get(RequestKey.ID.get())))
              .name((String) ((JSONObject) jsonPlayer).get(RequestKey.NAME.get()))
              .value((double) ((JSONObject) jsonPlayer).get(RequestKey.VALUE.get()))
              .build())
          .collect(Collectors.toList());

      playerRepository.saveAll(asd);

      System.out.println();
    };
  }

  private enum RequestKey {
    DATA("data"),
    VALUE("value"),
    NAME("pFName"),
    ID("id"),
    PLAYER_LIST("playerList");

    private final String key;

    RequestKey(String key) {
      this.key = key;
    }

    public String get() {
      return key;
    }
  }

}
