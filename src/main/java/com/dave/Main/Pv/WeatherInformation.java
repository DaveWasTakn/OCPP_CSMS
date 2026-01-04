package com.dave.Main.Pv;

import java.time.LocalDateTime;
import java.util.Date;

public interface WeatherInformation {

    LocalDateTime getSunriseTime(Date date);

    LocalDateTime getSunsetTime(Date date);

}
