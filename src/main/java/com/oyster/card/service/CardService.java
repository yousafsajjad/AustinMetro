package com.oyster.card.service;

import javax.ws.rs.NotFoundException;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.oyster.card.Repository.CardRepository;
import com.oyster.card.Repository.CardTransactionRepository;
import com.oyster.card.Rest.CardResponse;
import com.oyster.card.Rest.CardSwipe;
import com.oyster.card.data.Card;
import com.oyster.card.data.CardTransaction;
import com.oyster.card.domain.transaction.CardTransactionStatus;
import com.oyster.card.domain.transaction.TransactionHandler;

/**
 * Created by myousaf on 7/21/17.
 */
@Component
public class CardService {

    @Autowired
    private TransactionHandler transactionHandler;

    @Autowired
    private Mapper mapper;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardTransactionRepository cardTransactionRepository;

    @Transactional
    public CardResponse rechargeCard(String cardGUID, double amount) {
        validateCardExistence(cardGUID);
        validateAmount(amount);

        Card card = getCard(cardGUID);
        card.setBalance(card.getBalance() + amount);
        Card savedCard = cardRepository.save(card);
        CardResponse cardResponse = mapper.map(savedCard, CardResponse.class);
        return cardResponse;
    }

    @Transactional
    public CardResponse getBalance(String cardGUID) {
        validateCardExistence(cardGUID);
        Card card = getCard(cardGUID);
        return mapper.map(card, CardResponse.class);
    }

    @Transactional
    public CardResponse swipeIn(String cardGUID, CardSwipe cardSwipe) {
        validateCardExistence(cardGUID);
        Card card = getCard(cardGUID);
        completeAnyExistingTranaction(card.getCardGUID());
        return transactionHandler
            .getTransactionStrategy(cardSwipe.getTransportationMode())
            .swipeIn(card, cardSwipe);
    }

    private void completeAnyExistingTranaction(String cardGUID) {
        CardTransaction transaction = getInProgressTransaction(cardGUID);
        if (transaction != null) {
            transaction.setStatus(CardTransactionStatus.COMPLETE.name());
            cardTransactionRepository.save(transaction);
        }
    }

    private CardTransaction getInProgressTransaction(String cardGUID) {
        return cardTransactionRepository.findByCardGUIDAndInProgressStatus(cardGUID, CardTransactionStatus.IN_PRGRESS.name());
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalStateException("Invalid amount : " + amount);
        }
    }

    private void validateCardExistence(String cardGUID) {
        Card card = getCard(cardGUID);
        if (card == null) {
            throw new NotFoundException("Card not found for cardGUID: " + cardGUID);
        }
    }

    private Card getCard(String cardGUID) {
        return cardRepository.findByCardGUID(cardGUID);
    }

    public CardResponse swipeOut(String cardGUID, CardSwipe cardSwipe) {
        validateCardExistence(cardGUID);
        Card card = getCard(cardGUID);
        return transactionHandler
            .getTransactionStrategy(cardSwipe.getTransportationMode())
            .swipeOut(card, cardSwipe);
    }
}
