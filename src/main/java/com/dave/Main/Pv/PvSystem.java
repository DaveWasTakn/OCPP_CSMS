package com.dave.Main.Pv;

public interface PvSystem {

    int getCurrentKwhProduction();

    int getSmoothedKwhProduction();

    int getBatteryChargePercentage();

    int getBatteryChargeKwh();

}
