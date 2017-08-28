package com.oyster.card.config;

import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.oyster.card.Rest.CardResponse;
import com.oyster.card.data.Card;
import com.oyster.card.data.CardTransaction;

/**
 * Created by myousaf on 7/21/17.
 */
@Configuration
public class Config {

    BeanMappingBuilder objectMappingBuilder = new BeanMappingBuilder() {

        @Override
        protected void configure() {
            mapping(Card.class, CardResponse.class);
            mapping(CardTransaction.class, CardResponse.class);
        }
    };

    @Bean
    public DozerBeanMapper mapper() throws Exception {
        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.addMapping(objectMappingBuilder);
        return mapper;
    }
}
