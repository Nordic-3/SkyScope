package com.szte.SkyScope.Enums;

import java.util.Arrays;

public enum FlightOffersSortOptions {
    PRICE_ASC("priceAsc"),
    PRICE_DSC("priceDsc"),
    FLYTIME_ASC("flyTimeAsc"),
    FLYTIME_DSC("flyTimeDsc"),
    TRANSFERTIME_ASC("transferTimeAsc"),
    TRANSFERTIME_DSC("transferTimeDsc");

    private final String value;

    FlightOffersSortOptions(String value) {
        this.value = value;
    }
    public static FlightOffersSortOptions getOptionFromValue(String value) {
        return Arrays.stream(values())
                .filter(e -> e.value.equalsIgnoreCase(value))
                .findFirst()
                .get();
    }
}
