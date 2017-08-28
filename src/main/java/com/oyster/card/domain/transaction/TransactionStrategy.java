package com.oyster.card.domain.transaction;

import com.oyster.card.Rest.CardResponse;
import com.oyster.card.Rest.CardSwipe;
import com.oyster.card.data.Card;

/**
 * Created by myousaf on 7/21/17.
 */
public interface TransactionStrategy {

    public CardResponse swipeIn(Card card, CardSwipe cardSwipe);

    public CardResponse swipeOut(Card card, CardSwipe cardSwipe);
}
