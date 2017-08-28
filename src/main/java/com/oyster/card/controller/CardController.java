package com.oyster.card.controller;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.oyster.card.Rest.CardResponse;
import com.oyster.card.Rest.CardSwipe;
import com.oyster.card.service.CardService;

/**
 * Created by myousaf on 7/21/17.
 */
@RestController
public class CardController {

    @Autowired
    private CardService cardService;

    /**
     * Recharege the card
     *
     * @param cardGUID
     * @param amount
     * @return
     */
    @RequestMapping(path = "/card/{cardGUID}/recharge", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<CardResponse> rechargeCard(@PathVariable(name = "cardGUID") String cardGUID,
        @Valid @NotEmpty @RequestParam String amount) {
        CardResponse cardResponse = null;
        try {
            cardResponse = cardService.rechargeCard(cardGUID, Double.valueOf(amount));
        } catch (Exception ex) {
            // Exception needs to be handled better for validation and other exceptions thrown from service layer
        }
        return new ResponseEntity<CardResponse>(cardResponse, HttpStatus.OK);
    }

    /**
     * Get card balance
     *
     * @param cardGUID
     * @return
     */
    @RequestMapping(path = "/card/{cardGUID}/checkbalance", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<CardResponse> getBalance(@PathVariable(name = "cardGUID") String cardGUID) {
        CardResponse cardResponse = null;
        try {
            cardResponse = cardService.getBalance(cardGUID);
        } catch (Exception ex) {
            // Exception needs to be handled better for validation and other exceptions thrown from service layer
        }
        return new ResponseEntity<CardResponse>(cardResponse, HttpStatus.OK);
    }

    @RequestMapping(path = "/card/{cardGUID}/swipeIn", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<CardResponse> swipeIn(@PathVariable(name = "cardGUID") String cardGUID,
        @Valid @NotEmpty @RequestBody CardSwipe cardSwipe) {
        CardResponse cardResponse = null;
        try {
            cardResponse = cardService.swipeIn(cardGUID, cardSwipe);
        } catch (Exception ex) {
            // Exception needs to be handled better for validation and other exceptions thrown from service layer
        }
        return new ResponseEntity<CardResponse>(cardResponse, HttpStatus.OK);
    }

    @RequestMapping(path = "/card/{cardGUID}/swipeOut", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<CardResponse> swipeOut(@PathVariable(name = "cardGUID") String cardGUID,
        @Valid @NotEmpty @RequestBody CardSwipe cardSwipe) {
        CardResponse cardResponse = null;
        try {
            cardResponse = cardService.swipeOut(cardGUID, cardSwipe);
        } catch (Exception ex) {
            // Exception needs to be handled better for validation and other exceptions thrown from service layer
        }
        return new ResponseEntity<CardResponse>(cardResponse, HttpStatus.OK);
    }

}
