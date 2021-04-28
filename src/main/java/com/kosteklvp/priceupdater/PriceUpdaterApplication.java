package com.kosteklvp.priceupdater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PriceUpdaterApplication {

  public static final long MATCHDAY_ID = 12;

  public static void main(String[] args) {
    SpringApplication.run(PriceUpdaterApplication.class, args);
  }

}
