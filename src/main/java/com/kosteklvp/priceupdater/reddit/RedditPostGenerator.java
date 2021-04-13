package com.kosteklvp.priceupdater.reddit;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kosteklvp.priceupdater.model.Matchday;
import com.kosteklvp.priceupdater.repository.Players2MatchdaysRepo;

import lombok.AllArgsConstructor;

@Configuration
public class RedditPostGenerator {

  private static final Logger log = LoggerFactory.getLogger(RedditPostGenerator.class);

  @Bean
  public CommandLineRunner generateRedditPost(Players2MatchdaysRepo players2MatchdaysRepo) {
    return args -> {
      List<Object[]> priceChanges = players2MatchdaysRepo.getPriceChangesByMatchday(
          Matchday.builder().id(Long.valueOf(10)).build());

      List<Object[]> priceRises = priceChanges.stream()
          .filter(record -> ((BigDecimal) record[PriceChangeIndex.CHANGE.get()]).signum() > 0)
          .collect(Collectors.toList());
      List<Object[]> priceFalls = priceChanges.stream()
          .filter(record -> ((BigDecimal) record[PriceChangeIndex.CHANGE.get()]).signum() < 0)
          .collect(Collectors.toList());

      StringBuilder postText = new StringBuilder();

      postText.append(String.format("# Price risers (%s):", priceRises.size()));
      postText.append(generateTable(priceRises, PriceChangeType.RISE));

      postText.append(String.format("# Price fallers (%s):", priceFalls.size()));
      postText.append(generateTable(priceFalls, PriceChangeType.FALL));

      saveTextToFile(postText.toString(), "target/priceUpdates.txt");
    };
  }

  private void saveTextToFile(String text, String fileName) throws FileNotFoundException {
    PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName)));

    out.println(text);
    log.info("REDDIT POST SAVED IN \"{}\"", fileName);

    out.close();
  }

  private enum PriceChangeType {
    RISE, FALL;
  }

  private String generateTable(List<Object[]> priceUpdates, PriceChangeType priceChangeType) {
    StringBuilder table = new StringBuilder();
    table.append("&#x200B;\n\n");

    // headtitles
    List<String> columnTitles = List.of("Player", "Team", "Previous price", "Current price", "Price change");
    table.append("|");
    for (String title : columnTitles) {
      table.append(title);
      table.append("|");
    }
    table.append("\n").append("|");
    table.append(":-").append("|");
    table.append(":-").append("|");
    table.append("-:").append("|");
    table.append("-:").append("|");
    table.append("-:").append("|").append("\n");

    // rows
    for (Object[] record : priceUpdates) {
      table.append("|");
      table.append(record[PriceChangeIndex.PLAYER_NAME.get()]).append("|");
      table.append(record[PriceChangeIndex.CLUB_NAME.get()]).append("|");
      table.append(record[PriceChangeIndex.PREVIOUS_PRICE.get()]).append("|");
      table.append(record[PriceChangeIndex.CURRENT_PRICE.get()]).append("|");

      BigDecimal priceChange = (BigDecimal) record[PriceChangeIndex.CHANGE.get()];
      table.append("**")
          .append(PriceChangeType.RISE.equals(priceChangeType) ? "+" : "").append(priceChange)
          .append("**");
      table.append("|\n");
    }
    table.append("\n").append("&#x200B;").append("\n");

    log.info("GENERATED TABLE \"{}\"", priceChangeType);
    return table.toString();
  }

  @AllArgsConstructor
  private enum PriceChangeIndex {
    PLAYER_NAME(0),
    CLUB_NAME(1),
    PREVIOUS_PRICE(2),
    CURRENT_PRICE(3),
    CHANGE(4);

    private final int index;

    public int get() {
      return index;
    }
  }

}
