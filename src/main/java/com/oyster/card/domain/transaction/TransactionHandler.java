package com.oyster.card.domain.transaction;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oyster.card.domain.TransportationMode;
import com.oyster.card.domain.transaction.impl.BusTransactionStrategy;
import com.oyster.card.domain.transaction.impl.TubeTransactionStrategy;

/**
 * Created by myousaf on 7/21/17.
 */
@Component
public class TransactionHandler {

    @Autowired
    private BusTransactionStrategy busTransactionStrategy;

    @Autowired
    private TubeTransactionStrategy tubeTransactionStrategy;

    private Map<String, TransactionStrategy> transactionStrategyMap = new HashMap<>();

    @PostConstruct
    private void init() {
        transactionStrategyMap.put(TransportationMode.BUS.name(), busTransactionStrategy);
        transactionStrategyMap.put(TransportationMode.TUBE.name(), tubeTransactionStrategy);
    }

    public TransactionStrategy getTransactionStrategy(String transportationMode) {
        return transactionStrategyMap.get(transportationMode);
    }

}
