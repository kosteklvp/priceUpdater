package com.kosteklvp.priceupdater.reddit;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import com.kosteklvp.priceupdater.PriceUpdaterApplication;
import com.kosteklvp.priceupdater.model.Matchday;
import com.kosteklvp.priceupdater.repository.Players2MatchdaysRepo;
import com.kosteklvp.table.TableGenerator;
import com.kosteklvp.table.TableHeading;
import com.kosteklvp.table.TableRow;

import lombok.AllArgsConstructor;
import lombok.Builder;

public class RedditPostGenerator {

  private static final Logger log = LoggerFactory.getLogger(RedditPostGenerator.class);

  @Bean
  public CommandLineRunner generateRedditPost(Players2MatchdaysRepo players2MatchdaysRepo) {
    return args -> {
      List<Object[]> priceChanges = players2MatchdaysRepo.getPriceChangesByMatchday(
          Matchday.builder().id(PriceUpdaterApplication.MATCHDAY_ID).build());

      List<TableRow> priceRises = priceChanges.stream()
          .filter(record -> ((BigDecimal) record[PriceChangeTableHeading.PRICE_CHANGE.getIndex()]).signum() > 0)
          .map(getRecordToTableRowMapper())
          .collect(Collectors.toList());

      List<TableRow> priceFalls = priceChanges.stream()
          .filter(record -> ((BigDecimal) record[PriceChangeTableHeading.PRICE_CHANGE.getIndex()]).signum() < 0)
          .map(getRecordToTableRowMapper())
          .collect(Collectors.toList());

      StringBuilder postText = new StringBuilder();

      TableGenerator priceRisesTableGenerator = TableGenerator.builder()
          .headings(PriceChangeTableHeading.getAll())
          .rows(priceRises)
          .build();

      TableGenerator priceFallsTableGenerator = TableGenerator.builder()
          .headings(PriceChangeTableHeading.getAll())
          .rows(priceFalls)
          .build();

      postText.append(String.format("# Price risers (%s):", priceRises.size()));
      postText.append(priceRisesTableGenerator.generate());

      postText.append(String.format("# Price fallers (%s):", priceFalls.size()));
      postText.append(priceFallsTableGenerator.generate());

      saveTextToFile(postText.toString(), "target/priceUpdates.txt");
    };
  }

  private Function<Object[], PriceChangeRow> getRecordToTableRowMapper() {
    return record -> PriceChangeRow.builder()
        .playerName((String) record[PriceChangeTableHeading.PLAYER_NAME.getIndex()])
        .teamName((String) record[PriceChangeTableHeading.TEAM_NAME.getIndex()])
        .previousPrice(record[PriceChangeTableHeading.PREVIOUS_PRICE.getIndex()].toString())
        .currentPrice(record[PriceChangeTableHeading.CURRENT_PRICE.getIndex()].toString())
        .priceChange(record[PriceChangeTableHeading.PRICE_CHANGE.getIndex()].toString())
        .build();
  }

  private void saveTextToFile(String text, String fileName) throws FileNotFoundException {
    PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName)));

    out.println(text);
    log.info("REDDIT POST SAVED IN \"{}\"", fileName);

    out.close();
  }

  @AllArgsConstructor
  private enum PriceChangeTableHeading implements TableHeading {
    PLAYER_NAME("Player", Align.LEFT, 1),
    TEAM_NAME("Team", Align.LEFT, 2),
    PREVIOUS_PRICE("Previous price", Align.RIGHT, 3),
    CURRENT_PRICE("Current price", Align.RIGHT, 4),
    PRICE_CHANGE("Price change", Align.RIGHT, 5);

    private final String name;
    private final Align align;
    private final int index;

    @Override
    public String getName() {
      return name;
    }

    @Override
    public Align getAlign() {
      return align;
    }

    public int getIndex() {
      return index;
    }

    public static List<TableHeading> getAll() {
      return List.of(values());
    }
  }

  @Builder
  @AllArgsConstructor
  public static class PriceChangeRow implements TableRow {

    private final String playerName;
    private final String teamName;
    private final String previousPrice;
    private final String currentPrice;
    private final String priceChange;

    @Override
    public List<String> getValues() {
      return List.of(
          playerName,
          teamName,
          previousPrice,
          currentPrice,
          priceChange);
    }
  }

}
