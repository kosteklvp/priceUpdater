package com.kosteklvp.priceupdater.utilities;

import java.time.format.DateTimeFormatter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeUtil {
  public static final DateTimeFormatter FORMAT_DATE_TIME = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

}
