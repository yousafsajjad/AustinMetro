package com.oyster.card.domain.transaction.impl;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oyster.card.Repository.CardRepository;
import com.oyster.card.Repository.CardTransactionRepository;
import com.oyster.card.Rest.CardResponse;
import com.oyster.card.Rest.CardSwipe;
import com.oyster.card.builder.TransactionBuilder;
import com.oyster.card.data.Card;
import com.oyster.card.data.CardTransaction;
import com.oyster.card.domain.TransportationMode;
import com.oyster.card.domain.ZoneUtils;
import com.oyster.card.domain.fare.FareCalculator;
import com.oyster.card.domain.transaction.CardTransactionStatus;
import com.oyster.card.domain.transaction.TransactionStrategy;
import com.oyster.card.utils.ConstUtils;

/**
 * Created by myousaf on 7/21/17.
 */
@Component
public class TubeTransactionStrategy implements TransactionStrategy {

    @Autowired
    private Mapper mapper;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardTransactionRepository cardTransactionRepository;

    @Autowired
    private FareCalculator fareCalculator;

    @Override public CardResponse swipeIn(Card card, CardSwipe cardSwipe) {

        CardTransaction transaction = new TransactionBuilder()
            .withCard(card)
            .withCardAction(ConstUtils.ENTRY)
            .withStation(cardSwipe.getStation())
            .withFare(TransportationMode.valueOf(cardSwipe.getTransportationMode()).getFare())
            .build();

        Card savedCard = fareCalculator.chargeCardWithFare(card, transaction.getFare());
        CardTransaction cardTransaction = cardTransactionRepository.save(transaction);
        CardResponse cardResponse = mapper.map(cardTransaction, CardResponse.class);
        cardResponse.setBalance(savedCard.getBalance());
        return cardResponse;
    }

    @Override public CardResponse swipeOut(Card card, CardSwipe cardSwipe) {
        CardTransaction cardTransaction = getTransaction(card.getCardGUID());
        updateTransaction(cardTransaction, cardSwipe);
        final double fare =
            fareCalculator.calculateFare(ZoneUtils.convertToList(cardTransaction.getStartZone()),
                ZoneUtils.convertToList(cardTransaction.getEndZone()));
        cardTransaction.setStatus(CardTransactionStatus.COMPLETE.name());
        card.setBalance(card.getBalance() + cardTransaction.getFare());
        cardTransaction.setFare(fare);
        Card savedCard = fareCalculator.chargeCardWithFare(card, fare);
        cardTransaction = cardTransactionRepository.save(cardTransaction);
        CardResponse cardResponse = mapper.map(cardTransaction, CardResponse.class);
        cardResponse.setBalance(savedCard.getBalance());
        return cardResponse;
    }

    private CardTransaction getTransaction(String cardGUID) {
        return cardTransactionRepository.findByCardGUID(cardGUID);
    }

    private void updateTransaction(CardTransaction cardTransaction, CardSwipe cardSwipe) {
        cardTransaction.setEndStation(cardSwipe.getStation());
        cardTransaction.setEndZone(String.join(",", ZoneUtils.getZone(cardSwipe.getStation())));
    }
}
