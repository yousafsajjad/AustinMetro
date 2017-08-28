package com.oyster.card.domain.fare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oyster.card.Repository.CardRepository;
import com.oyster.card.data.Card;
import com.oyster.card.domain.fare.impl.AnyTwoZoneExcludingZoneOne;
import com.oyster.card.domain.fare.impl.AnyTwoZonesIncludingZoneOne;
import com.oyster.card.domain.fare.impl.AnyoneZoneOutsideZoneOne;
import com.oyster.card.domain.fare.impl.AnywhereInZoneOne;

/**
 * Created by myousaf on 7/21/17.
 */
@Component
public class FareCalculator {

    @Autowired
    private AnywhereInZoneOne anywhereInZoneOne;

    @Autowired
    private AnyoneZoneOutsideZoneOne anyoneZoneOutsideZoneOne;

    @Autowired
    private AnyTwoZonesIncludingZoneOne anyTwoZonesIncludingZoneOne;

    @Autowired
    private AnyTwoZoneExcludingZoneOne anyTwoZoneExcludingZoneOne;

    @Autowired
    private CardRepository cardRepository;

    private List<FareRuleEngine> ruleEngineList = new ArrayList<>();

    @PostConstruct
    private void init() {
        ruleEngineList.addAll(Arrays
            .asList(anywhereInZoneOne,
                anyoneZoneOutsideZoneOne,
                anyTwoZoneExcludingZoneOne,
                anyTwoZonesIncludingZoneOne));
    }

    public double calculateFare(List<String> startZones, List<String> endZones) {
        for (FareRuleEngine rule : ruleEngineList) {
            double fare = rule.process(startZones, endZones);
            if (fare != 0) {
                return fare;
            }
        }
        return 0;
    }

    public Card chargeCardWithFare(Card card, double fare) {
        card.setBalance(card.getBalance() - fare);
        if (card.getBalance() < 0) {
            throw new IllegalStateException("Insufficient balance on card: " + card.getCardGUID());
        }
        return this.cardRepository.save(card);
    }

}
