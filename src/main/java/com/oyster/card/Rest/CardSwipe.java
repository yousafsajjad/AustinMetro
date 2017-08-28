package com.oyster.card.Rest;

/**
 * Created by myousaf on 7/21/17.
 */
public class CardSwipe {

    private String station;

    // BUS/TUBE
    private String transportationMode;

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getTransportationMode() {
        return transportationMode;
    }

    public void setTransportationMode(String transportationMode) {
        this.transportationMode = transportationMode;
    }
}
