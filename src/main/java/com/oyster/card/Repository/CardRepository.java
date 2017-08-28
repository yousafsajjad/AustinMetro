package com.oyster.card.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oyster.card.data.Card;

/**
 * Created by myousaf on 7/21/17.
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("Select c From Card c where c.cardGUID = ?1")
    Card findByCardGUID(@Param("cardGUID") String cardGUID);
}
