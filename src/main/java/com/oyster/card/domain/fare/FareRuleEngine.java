package com.oyster.card.domain.fare;

import java.util.List;

/**
 * Created by myousaf on 7/21/17.
 */
public interface FareRuleEngine {

    double process(List<String> startZone, List<String> endZone);
}
