package com.oyster.card.com.oyster.card;

import javax.ws.rs.NotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.oyster.card.Repository.CardRepository;
import com.oyster.card.Repository.CardTransactionRepository;
import com.oyster.card.Rest.CardResponse;
import com.oyster.card.Rest.CardSwipe;
import com.oyster.card.data.Card;
import com.oyster.card.data.CardTransaction;
import com.oyster.card.domain.TransportationMode;
import com.oyster.card.domain.ZoneUtils;
import com.oyster.card.domain.transaction.CardTransactionStatus;
import com.oyster.card.service.CardService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by myousaf on 7/24/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CardServiceTest {

    @MockBean
    private CardRepository mockCardRepository;

    @MockBean
    private CardTransactionRepository mockCardTransactionRepository;

    @Autowired
    private CardService cardService;

    private static final String CARD_GUID = "123-456";

    private static final double AMOUNT = 10.0;

    @Test
    public void testCardRecharege() {
        Card savedCard = buildCardWithZeroAmount();

        when(mockCardRepository.findByCardGUID(CARD_GUID)).thenReturn(savedCard);
        when(mockCardRepository.save(any(Card.class))).thenReturn(savedCard);
        CardResponse cardResponse = cardService.rechargeCard(CARD_GUID, AMOUNT);
        assertEquals(cardResponse.getCardGUID(), CARD_GUID);
        assertEquals(cardResponse.getBalance(), AMOUNT, 1);
    }

    @Test(expected = NotFoundException.class)
    public void testCardNotFound() {
        cardService.rechargeCard(CARD_GUID, AMOUNT);
    }

    @Test
    public void testViewBalance() {
        Card savedCard = buildCardWithTenPounds();
        when(mockCardRepository.findByCardGUID(CARD_GUID)).thenReturn(savedCard);
        when(mockCardRepository.save(any(Card.class))).thenReturn(savedCard);
        CardResponse cardResponse = cardService.getBalance(CARD_GUID);
        assertEquals(cardResponse.getCardGUID(), CARD_GUID);
        assertEquals(cardResponse.getBalance(), AMOUNT, 1);
    }

    @Test
    public void testBusTripSwipeIn() {
        Card card = buildCardWithTenPounds();
        CardSwipe cardSwipe = buildBusSwipeInZoneOneRequest();
        CardTransaction cardTransaction = buildBusSwipeInCardTransaction(card, cardSwipe);
        when(mockCardRepository.findByCardGUID(CARD_GUID)).thenReturn(card);
        when(mockCardTransactionRepository.findByCardGUID(CARD_GUID)).thenReturn(null);
        when(mockCardRepository.save(card)).thenReturn(card);
        when(mockCardTransactionRepository.save(any(CardTransaction.class))).thenReturn(cardTransaction);
        CardResponse cardResponse = cardService.swipeIn(CARD_GUID, cardSwipe);
        assertEquals(cardResponse.getBalance(), card.getBalance(), 1);
        assertEquals(cardResponse.getCardGUID(), card.getCardGUID());
        assertEquals(cardResponse.getStatus(), cardTransaction.getStatus());
    }

    @Test
    public void testBusTripSwipeOut() {
        Card card = buildCardWithTenPounds();
        CardSwipe cardSwipe = buildBusSwipeOutZoneOneRequest();
        CardTransaction cardTransaction = buildBusSwipeOutCardTransaction(card, cardSwipe);
        when(mockCardRepository.findByCardGUID(CARD_GUID)).thenReturn(card);
        when(mockCardTransactionRepository.findByCardGUID(CARD_GUID)).thenReturn(cardTransaction);
        when(mockCardTransactionRepository.save(any(CardTransaction.class))).thenReturn(cardTransaction);
        CardResponse cardResponse = cardService.swipeOut(CARD_GUID, cardSwipe);
        assertEquals(cardResponse.getBalance(), card.getBalance(), 1);
        assertEquals(cardResponse.getCardGUID(), card.getCardGUID());
        assertEquals(cardResponse.getStatus(), cardTransaction.getStatus());
    }

    @Test
    public void testTubeTripWithZoneOneSwipeIn() {
        Card card = buildCardWithTenPounds();
        CardSwipe cardSwipe = buildBusSwipeInZoneOneRequest();
        CardTransaction cardTransaction = buildTubeSwipeInCardTransaction(card, cardSwipe);
        when(mockCardRepository.findByCardGUID(CARD_GUID)).thenReturn(card);
        when(mockCardTransactionRepository.findByCardGUID(CARD_GUID)).thenReturn(null);
        when(mockCardRepository.save(card)).thenReturn(card);
        when(mockCardTransactionRepository.save(any(CardTransaction.class))).thenReturn(new CardTransaction());
        CardResponse cardResponse = cardService.swipeIn(CARD_GUID, cardSwipe);
        assertEquals(cardResponse.getBalance(), card.getBalance(), 1);
        assertEquals(cardResponse.getCardGUID(), cardResponse.getCardGUID());
    }

    @Test
    public void testTubeTripWithZoneOneSwipeOut() {
        Card card = buildCardWithTenPounds();
        CardSwipe cardSwipe = buildTubeSwipeOutZoneOneRequest();
        CardTransaction cardTransaction = buildTubeSwipeOutZoneOneTransaction(card, cardSwipe);
        when(mockCardRepository.findByCardGUID(CARD_GUID)).thenReturn(card);
        when(mockCardTransactionRepository.findByCardGUID(CARD_GUID)).thenReturn(cardTransaction);
        when(mockCardRepository.save(any(Card.class))).thenReturn(card);
        when(mockCardTransactionRepository.save(any(CardTransaction.class))).thenReturn(cardTransaction);
        CardResponse cardResponse = cardService.swipeOut(CARD_GUID, cardSwipe);
        assertEquals(cardResponse.getBalance(), card.getBalance(), 1);
    }

    private CardTransaction buildTubeSwipeOutZoneOneTransaction(Card card, CardSwipe cardSwipe) {
        CardTransaction cardTransaction = new CardTransaction();
        cardTransaction.setCardGUID(card.getCardGUID());
        cardTransaction.setFare(TransportationMode.valueOf(cardSwipe.getTransportationMode()).getFare());
        cardTransaction.setEndStation(cardSwipe.getStation());
        cardTransaction.setEndZone(ZoneUtils.covertToString(ZoneUtils.getZone(cardSwipe.getStation())));
        cardTransaction.setStatus(CardTransactionStatus.COMPLETE.name());
        return cardTransaction;
    }

    private CardSwipe buildTubeSwipeOutZoneOneRequest() {
        CardSwipe cardSwipe = new CardSwipe();
        cardSwipe.setStation(ZoneUtils.Station.EarlsCourt.getStation());
        cardSwipe.setTransportationMode(TransportationMode.BUS.name());
        return cardSwipe;
    }

    private CardTransaction buildTubeSwipeInCardTransaction(Card card, CardSwipe cardSwipe) {
        CardTransaction cardTransaction = new CardTransaction();
        cardTransaction.setCardGUID(card.getCardGUID());
        cardTransaction.setFare(TransportationMode.valueOf(cardSwipe.getTransportationMode()).getFare());
        cardTransaction.setStartStation(cardSwipe.getStation());
        cardTransaction.setStartZone(ZoneUtils.covertToString(ZoneUtils.getZone(cardSwipe.getStation())));
        cardTransaction.setStatus(CardTransactionStatus.IN_PRGRESS.name());
        return cardTransaction;
    }

    private CardTransaction buildBusSwipeInCardTransaction(Card card, CardSwipe cardSwipe) {
        CardTransaction cardTransaction = new CardTransaction();
        cardTransaction.setCardGUID(card.getCardGUID());
        cardTransaction.setFare(TransportationMode.valueOf(cardSwipe.getTransportationMode()).getFare());
        cardTransaction.setStartStation(cardSwipe.getStation());
        cardTransaction.setStartZone(ZoneUtils.covertToString(ZoneUtils.getZone(cardSwipe.getStation())));
        cardTransaction.setStatus(CardTransactionStatus.IN_PRGRESS.name());
        return cardTransaction;
    }

    private CardTransaction buildBusSwipeOutCardTransaction(Card card, CardSwipe cardSwipe) {
        CardTransaction cardTransaction = new CardTransaction();
        cardTransaction.setCardGUID(card.getCardGUID());
        cardTransaction.setFare(TransportationMode.valueOf(cardSwipe.getTransportationMode()).getFare());
        cardTransaction.setEndStation(cardSwipe.getStation());
        cardTransaction.setEndZone(ZoneUtils.covertToString(ZoneUtils.getZone(cardSwipe.getStation())));
        cardTransaction.setStatus(CardTransactionStatus.COMPLETE.name());
        return cardTransaction;
    }

    private CardSwipe buildBusSwipeInZoneOneRequest() {
        CardSwipe cardSwipe = new CardSwipe();
        cardSwipe.setStation(ZoneUtils.Station.Holborn.getStation());
        cardSwipe.setTransportationMode(TransportationMode.BUS.name());
        return cardSwipe;
    }

    private CardSwipe buildTubeSwipeInZoneOneRequest() {
        CardSwipe cardSwipe = new CardSwipe();
        cardSwipe.setStation(ZoneUtils.Station.Holborn.getStation());
        cardSwipe.setTransportationMode(TransportationMode.BUS.name());
        return cardSwipe;
    }

    private CardSwipe buildBusSwipeOutZoneOneRequest() {
        CardSwipe cardSwipe = new CardSwipe();
        cardSwipe.setStation(ZoneUtils.Station.EarlsCourt.getStation());
        cardSwipe.setTransportationMode(TransportationMode.BUS.name());
        return cardSwipe;
    }

    private CardTransaction buildBusSwipeInCardTransaction(String cardGUID) {
        CardTransaction cardTransaction = new CardTransaction();
        cardTransaction.setCardGUID(cardGUID);
        cardTransaction.setStatus(CardTransactionStatus.IN_PRGRESS.name());
        return cardTransaction;
    }

    private Card buildCardWithZeroAmount() {
        Card card = new Card();
        card.setCardGUID(CARD_GUID);
        return card;
    }

    private Card buildCardWithTenPounds() {
        Card card = new Card();
        card.setCardGUID(CARD_GUID);
        card.setBalance(AMOUNT);
        return card;
    }

}
