package com.oyster.card.Rest;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by myousaf on 7/21/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardResponse {

    private String cardGUID;

    private double fare;

    private double balance;

    private String startStation;

    private String startZone;

    private String endStation;

    private String endZone;

    private String status;

    public String getCardGUID() {
        return cardGUID;
    }

    public void setCardGUID(String cardGUID) {
        this.cardGUID = cardGUID;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getStartZone() {
        return startZone;
    }

    public void setStartZone(String startZone) {
        this.startZone = startZone;
    }

    public String getEndZone() {
        return endZone;
    }

    public void setEndZone(String endZone) {
        this.endZone = endZone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }
}
