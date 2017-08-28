package com.oyster.card.domain.fare.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.oyster.card.domain.fare.FareRuleEngine;

/**
 * Created by myousaf on 7/21/17.
 */
@Component
public class AnyTwoZoneExcludingZoneOne implements FareRuleEngine {

    @Override public double process(List<String> startZone, List<String> endZone) {
        if (!startZone.contains("1") && !endZone.contains("1")) {
            return 2.25;
        }
        return 0;
    }
}
