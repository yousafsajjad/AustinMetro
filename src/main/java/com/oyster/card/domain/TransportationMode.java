package com.oyster.card.domain;

/**
 * Created by myousaf on 7/21/17.
 */
public enum TransportationMode {
    BUS(1.80),
    TUBE(3.20);

    private double fare;

    TransportationMode(double fare) {
        this.fare = fare;
    }

    public double getFare() {
        return this.fare;
    }
}
