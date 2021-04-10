package com.kosteklvp.priceupdater.utilities;

import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UEFAGaming {
  private static final String MAIN_URL = "https://gaming.uefa.com/en/uclfantasy";

  public static final String DEFAULT_LANGUAGE = "en";

  @AllArgsConstructor
  public enum ServiceURL {
    PLAYERS(MAIN_URL + "/services/api/Feed/players"),
    LIVE_MIXED(MAIN_URL + "/services/api/Live/mixed");

    private final String url;

    public String get() {
      return url;
    }

  }

  @AllArgsConstructor
  public enum JSONKey {
    DATA("data"),
    VALUE("value"),
    ID("id");

    private final String key;

    public String get() {
      return key;
    }
  }

  @AllArgsConstructor
  public enum LiveMixedJSONKey {
    DEADLINE("Deadline"),
    FIXTURE("Fixture"),
    MATCHDAY_ID("mdId");

    private final String key;

    public String get() {
      return key;
    }
  }

  @AllArgsConstructor
  public enum PlayersJSONKey {
    NAME("pFName"),
    ID("id"),
    VALUE("value"),
    TEAM_ID("tId"),
    TEAM_CODE("cCode"),
    TEAM_NAME("tName"),
    PLAYER_LIST("playerList"),
    GAMEDAY_ID("gamedayId"),
    LANGUAGE("language");

    private final String key;

    public String get() {
      return key;
    }
  }
}
