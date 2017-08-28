package com.oyster.card.builder;

import com.oyster.card.data.Card;
import com.oyster.card.data.CardTransaction;
import com.oyster.card.domain.ZoneUtils;
import com.oyster.card.domain.transaction.CardTransactionStatus;
import com.oyster.card.utils.ConstUtils;

/**
 * Created by myousaf on 7/21/17.
 */
public class TransactionBuilder {

    private Card card;

    private String action;

    private String station;

    private double fare;

    public TransactionBuilder withCard(Card card) {
        this.card = card;
        return getSelf();
    }

    public TransactionBuilder getSelf() {
        return this;
    }

    public CardTransaction build() {
        CardTransaction cardTransaction = new CardTransaction();
        cardTransaction.setCardGUID(card.getCardGUID());
        if (this.action.equals(ConstUtils.ENTRY)) {
            cardTransaction.setStartStation(this.station);
            cardTransaction.setStartZone(String.join(",", ZoneUtils.getZone(this.station)));
            cardTransaction.setFare(fare);
            cardTransaction.setStatus(CardTransactionStatus.IN_PRGRESS.name());
        } else {
            cardTransaction.setEndStation(this.station);
            cardTransaction.setEndZone(String.join(",", ZoneUtils.getZone(this.station).toString()));
        }
        return cardTransaction;
    }

    public TransactionBuilder withStation(String station) {
        this.station = station;
        return getSelf();
    }

    public TransactionBuilder withCardAction(String action) {
        this.action = action;
        return getSelf();
    }

    public TransactionBuilder withFare(double fare) {
        this.fare = fare;
        return getSelf();
    }
}
